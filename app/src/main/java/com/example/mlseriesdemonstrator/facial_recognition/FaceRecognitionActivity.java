package com.example.mlseriesdemonstrator.facial_recognition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.student.FingerPrintAuthActivity;
import com.example.mlseriesdemonstrator.facial_recognition.the_vision.VisionBaseProcessor;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;

public class FaceRecognitionActivity extends MLVideoHelperActivity implements FaceRecognitionProcessor.FaceRecognitionCallback {

  private static final String TAG = "FaceRecognitionActivity";
  private Interpreter faceNetInterpreter;
  private FaceRecognitionProcessor faceRecognitionProcessor;
  private Face face;
  private Bitmap faceBitmap;
  private float[] faceVector;
  Context context;
  public String mode;
  public String eventId;
  Button fingerPrintScreenBtn;
  TextView fpTxt;
  public String studentId;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String updateFace = "update face";
    mode = getIntent().getStringExtra("mode");
    eventId = getIntent().getStringExtra("event_id");
    fingerPrintScreenBtn = findViewById(R.id.FINGER_PRINT);
    fpTxt = findViewById(R.id.FP_TEXT);
    studentId = getIntent().getStringExtra("stud_id");

    context = FaceRecognitionActivity.this;

    if (Utility.getUser().getRole().equals("admin")) {
      fingerPrintScreenBtn.setVisibility(View.GONE);
      fpTxt.setVisibility(View.GONE);
    }

    assert mode != null;
    if (mode.equals(updateFace)) {
      makeAddFaceVisible();
      previewView.setVisibility(View.VISIBLE);
      graphicOverlay.setVisibility(View.VISIBLE);
      fingerPrintScreenBtn.setVisibility(View.GONE);
      fpTxt.setVisibility(View.GONE);
    } else {
      Intent intent = new Intent(this, FingerPrintAuthActivity.class);

      intent.putExtra("event_id", eventId);

      fingerPrintScreenBtn.setOnClickListener(v -> startActivity(intent));
    }
  }
  @Override
  protected VisionBaseProcessor setProcessor() {
    try {
      faceNetInterpreter = new Interpreter(FileUtil.loadMappedFile(this, "mobile_face_net.tflite"), new Interpreter.Options());
    } catch (IOException e) {
      e.printStackTrace();
    }

    faceRecognitionProcessor = new FaceRecognitionProcessor(
            faceNetInterpreter,
            graphicOverlay,
            this
    );

    faceRecognitionProcessor.faceRecognitionActivity = this;

    return faceRecognitionProcessor;
  }

  @Override
  public void onFaceDetected(Face face, Bitmap faceBitmap, float[] faceVector) {
    this.face = face;
    this.faceBitmap = faceBitmap;
    this.faceVector = faceVector;
  }

  @Override
  public void onFaceRecognised(Face face, float probability, String name) {

  }

  @Override
  public void onAddFaceClicked(View view) {
    super.onAddFaceClicked(view);

    if (face == null || faceBitmap == null) {
      return;
    }

    Face tempFace = face;
    Log.d(TAG, "dis da temp face" + tempFace);
    Bitmap tempBitmap = faceBitmap;
    float[] tempVector = faceVector;

    LayoutInflater inflater = LayoutInflater.from(this);
    View dialogView = inflater.inflate(R.layout.add_face_dialog, null);

    ((ImageView) dialogView.findViewById(R.id.dlg_image)).setImageBitmap(tempBitmap);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setView(dialogView);
    builder.setPositiveButton("Save", (dialog, which) -> {
      faceRecognitionProcessor.registerFace(tempVector);
      Utility.showToast(context, "Face updated!");
      finish();
    }).setNegativeButton("Cancel", (dialog, which) -> {});
    builder.show();
  }

}