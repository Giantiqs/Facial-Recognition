package com.example.mlseriesdemonstrator.interfaces;

import com.example.mlseriesdemonstrator.classes.User;

public interface UserFetchCallback {
    void onUserFetched(User user);
    void onUserFetchFailure();
}