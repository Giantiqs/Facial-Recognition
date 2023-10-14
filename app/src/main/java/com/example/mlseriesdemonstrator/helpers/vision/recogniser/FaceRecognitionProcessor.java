package com.example.mlseriesdemonstrator.helpers.vision.recogniser;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageProxy;

import com.example.mlseriesdemonstrator.facial_recognition.FaceRecognitionActivity;
import com.example.mlseriesdemonstrator.helpers.vision.FaceGraphic;
import com.example.mlseriesdemonstrator.helpers.vision.GraphicOverlay;
import com.example.mlseriesdemonstrator.helpers.vision.VisionBaseProcessor;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FaceRecognitionProcessor extends VisionBaseProcessor<List<Face>> {

  static class Person {
    public String name;
    public List<Float> faceVector;

    public Person() {
      // Default constructor
    }

    public Person(String name, List<Float> faceVector) {
      this.name = name;
      this.faceVector = faceVector;
    }
  }

  public interface FaceRecognitionCallback {
    void onFaceRecognised(Face face, float probability, String name);
    void onFaceDetected(Face face, Bitmap faceBitmap, float[] vector);
  }

  private static final String TAG = "FaceRecognitionProcessor";

  // Input image size for our facenet model
  private static final int FACENET_INPUT_IMAGE_SIZE = 112;

  private final FaceDetector detector;
  private final Interpreter faceNetModelInterpreter;
  private final ImageProcessor faceNetImageProcessor;
  private final GraphicOverlay graphicOverlay;
  private final FaceRecognitionCallback callback;
  public FaceRecognitionActivity faceRecognitionActivity;
  private final Map<String, Person> recognisedFaceMap = new HashMap<>();

  public FaceRecognitionProcessor(Interpreter faceNetModelInterpreter,
                                  GraphicOverlay graphicOverlay,
                                  FaceRecognitionCallback callback) {
    this.callback = callback;
    this.graphicOverlay = graphicOverlay;
    // initialize processors
    this.faceNetModelInterpreter = faceNetModelInterpreter;
    faceNetImageProcessor = new ImageProcessor.Builder()
            .add(new ResizeOp(
                    FACENET_INPUT_IMAGE_SIZE,
                    FACENET_INPUT_IMAGE_SIZE,
                    ResizeOp.ResizeMethod.BILINEAR
            )).add(new NormalizeOp(0f, 255f))
            .build();

    FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            // to ensure we don't count and analyse same person again
            .enableTracking()
            .build();
    detector = FaceDetection.getClient(faceDetectorOptions);

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("faces");

    updateRecognisedFaceMap(collectionRef);
  }

  @OptIn(markerClass = ExperimentalGetImage.class)
  public Task<List<Face>> detectInImage(ImageProxy imageProxy,
                                        Bitmap bitmap,
                                        int rotationDegrees) {

    InputImage inputImage = InputImage.fromMediaImage(
            Objects.requireNonNull(imageProxy.getImage()), rotationDegrees
    );

    // In order to correctly display the face bounds, the orientation of the analyzed
    // image and that of the viewfinder have to match. Which is why the dimensions of
    // the analyzed image are reversed if its rotation information is 90 or 270.

    boolean reverseDimens = rotationDegrees == 90 || rotationDegrees == 270;
    int width;
    int height;

    if (reverseDimens) {
      width = imageProxy.getHeight();
      height =  imageProxy.getWidth();
    } else {
      width = imageProxy.getWidth();
      height = imageProxy.getHeight();
    }
    return detector.process(inputImage)
            .addOnSuccessListener(faces -> {

              graphicOverlay.clear();

              for (Face face : faces) {
                FaceGraphic faceGraphic = new FaceGraphic(
                        graphicOverlay,
                        face,
                        false,
                        width,
                        height
                );

//                        Log.d(TAG, "face found, id: " + face.getTrackingId());
//
//                            if (activity != null) {
//                                activity.setTestImage(cropToBBox(
//                                        bitmap,
//                                        face.getBoundingBox(),
//                                        rotationDegrees
//                                ));
//                            }

                // now we have a face, so we can use that to analyse age and gender

                Bitmap faceBitmap = cropToBBox(
                        bitmap,
                        face.getBoundingBox(),
                        rotationDegrees
                );

                if (faceBitmap == null) {
                  Log.d("GraphicOverlay", "Face bitmap null");
                  return;
                }

                TensorImage tensorImage = TensorImage.fromBitmap(faceBitmap);
                ByteBuffer faceNetByteBuffer = faceNetImageProcessor
                        .process(tensorImage)
                        .getBuffer();

                float[][] faceOutputArray = new float[1][192];

                faceNetModelInterpreter.run(faceNetByteBuffer, faceOutputArray);

                Log.d(TAG, "output array: " + Arrays.deepToString(faceOutputArray));

                if (callback != null) {
                  callback.onFaceDetected(face, faceBitmap, faceOutputArray[0]);
                  if (!recognisedFaceMap.isEmpty()) {
                    Pair<String, Float> result = findNearestFace(faceOutputArray[0]);
                    // if distance is within confidence
                    if (result.second < 1.0f) {
                      faceGraphic.name = result.first;
                      callback.onFaceRecognised(face, result.second, result.first);
                      // Add to the "attendance" collection when a recognized face is found
                      addToAttendance(result.first);
                    }
                  }
                }

                graphicOverlay.add(faceGraphic);
              }
            })
            .addOnFailureListener(e -> {
              // intentionally left empty
            });
  }

  // looks for the nearest vector in the dataset (using L2 norm)
  // and returns the pair <name, distance>
  private Pair<String, Float> findNearestFace(float[] vector) {
    Pair<String, Float> ret = null;
    float minDistance = Float.MAX_VALUE;

    for (Map.Entry<String, Person> entry : recognisedFaceMap.entrySet()) {
      final String name = entry.getKey();
      final List<Float> knownVector = entry.getValue().faceVector;

      float distance = 0;
      for (int i = 0; i < vector.length; i++) {
        float diff = vector[i] - knownVector.get(i);
        distance += diff * diff;
      }
      distance = (float) Math.sqrt(distance);

      if (distance < minDistance) {
        minDistance = distance;
        ret = new Pair<>(name, distance);
      }
    }

    return ret;
  }


  public void stop() {
    detector.close();
  }

  private Bitmap cropToBBox(Bitmap image, Rect boundingBox, int rotation) {
    int shift = 0;
    if (rotation != 0) {
      Matrix matrix = new Matrix();
      matrix.postRotate(rotation);
      image = Bitmap.createBitmap(
              image,
              0,
              0,
              image.getWidth(),
              image.getHeight(),
              matrix,
              true
      );
    }
    if (boundingBox.top >= 0 && boundingBox.bottom <= image.getWidth()
            && boundingBox.top + boundingBox.height() <= image.getHeight()
            && boundingBox.left >= 0
            && boundingBox.left + boundingBox.width() <= image.getWidth()) {
      return Bitmap.createBitmap(
              image,
              boundingBox.left,
              boundingBox.top + shift,
              boundingBox.width(),
              boundingBox.height()
      );
    } else return null;
  }

  // Register a name against the vector

  public void registerFace(Editable input, float[] tempVector) {

    User user = Utility.getUser();

    List<Float> vectorList = new ArrayList<>();
    for (float value : tempVector) {
      vectorList.add(value);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("faces");
    Person person = new Person(user.getFirstName() + user.getLastName(), vectorList);

    collectionRef.document(user.getInstitutionalID())
            .set(person)
            .addOnSuccessListener(documentReference -> {
              Log.d(TAG, "Document added with ID: " + user.getInstitutionalID());
              // Handle success case here
            })
            .addOnFailureListener(e -> {
              Log.e(TAG, "Error adding document", e);
              // Handle error case here
            });

    CollectionReference collectionReference = Utility.getUserRef();

    collectionReference
            .document(user.getUID())
            .update("faceVector", vectorList)
            .addOnSuccessListener(unused -> Log.d(TAG, "user face has set"))
            .addOnFailureListener(e -> Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage())));

    // Update the recognisedFaceList
    updateRecognisedFaceMap(collectionRef);
  }

  public void updateFace(float[] tempVector) {
    // Retrieve the current user
    User user = Utility.getUser();

    // Create a list to store the face vector
    List<Float> vectorList = new ArrayList<>();
    for (float value : tempVector) {
      vectorList.add(value);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Update the user's face vector in the "faces" collection
    CollectionReference collectionRef = db.collection("faces");

    Person person = new Person(user.getFirstName() + user.getLastName(), vectorList);

    // Add the user to the "faces" collection with a document ID based on user's UID
    collectionRef.document(user.getUID())
            .set(person)
            .addOnSuccessListener(documentReference -> {
              Log.d(TAG, "Document added/updated with ID: " + user.getUID());
              // Handle success case here
            })
            .addOnFailureListener(e -> {
              Log.e(TAG, "Error adding/updating document", e);
              // Handle error case here
            });

    // Update the user's face vector in Firestore
    CollectionReference collectionReference = Utility.getUserRef();

    // Update the "faceVector" field in the user's Firestore document
    collectionReference
            .document(user.getUID())
            .update("faceVector", vectorList)
            .addOnSuccessListener(unused -> {
              Log.d(TAG, "User's face vector has been updated in Firestore");
              // Update the recognisedFaceList if needed
              updateRecognisedFaceMap(collectionRef);
            })
            .addOnFailureListener(e -> Log.e(TAG, Objects.requireNonNull(e.getLocalizedMessage())));
  }

  private void updateRecognisedFaceMap(CollectionReference collectionRef) {
    collectionRef.get()
            .addOnSuccessListener(
                    queryDocumentSnapshots -> {
                      recognisedFaceMap.clear();
                      for (Person person : queryDocumentSnapshots.toObjects(Person.class)) {
                        recognisedFaceMap.put(person.name, person);
                      }
                    }
            )
            .addOnFailureListener(e -> {
              Log.e(TAG, "Error getting documents", e);
              // Handle error case here
            });
  }

  public void addToAttendance(String personName) {
    if (personName != null) {
      // Assuming you have a Firestore reference to the "attendance" collection
      FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
      CollectionReference attendanceCollectionRef = fireStore.collection("attendance");
      Map<String, Object> attendanceData = new HashMap<>();
      User user = Utility.getUser();

      attendanceData.put("name", personName);
      attendanceData.put("timestamp", FieldValue.serverTimestamp());

      attendanceCollectionRef.document(user.getInstitutionalID())
              .set(attendanceData)
              .addOnSuccessListener(documentReference -> {
                Log.d(TAG, "Added to attendance: " + personName);
                // Handle success case here
              })
              .addOnFailureListener(e -> {
                Log.e(TAG, "Error adding to attendance", e);
                // Handle error case here
              });
    }
  }

}