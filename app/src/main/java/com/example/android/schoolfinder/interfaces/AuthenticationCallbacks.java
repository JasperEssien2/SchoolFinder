package com.example.android.schoolfinder.interfaces;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationCallbacks {

    void login(boolean loggedInSuccessful, FirebaseUser user);

    void signUp(boolean signedUpSuccessful, FirebaseUser user);

    void userInsertedToDatabase(Users users);

    void userInsertedToDatabase(School school);

    void loggedOut();

    void userGotten(School school);

    void userGotten(Users users);
}
