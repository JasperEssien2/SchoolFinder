package com.example.android.schoolfinder.FirebaseHelper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.BaseAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Authentication implements BaseAuthentication {


    private static final String TAG = Authentication.class.getSimpleName();
    private FirebaseAuth mAuth;
    private AuthenticationCallbacks mCallbacks;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference reference;

    public Authentication(AuthenticationCallbacks callbacks) {
        mCallbacks = callbacks;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        reference = mFirebaseDatabase.getReference();

    }

    @Override
    public void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "SignedUp completed and is successful ooh ? ");
                            mCallbacks.signUp(true,
                                    task.getResult() != null ? task.getResult().getUser() : null);
                        } else {
                            mCallbacks.signUp(false, null);
                            Log.e(TAG, "SignedUp completed and is NOT successful ooh ? ");
                        }
                    }
                });
    }

    @Override
    public void putNewUserInDb(final School school) {
        reference.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                .setValue(school)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            mCallbacks.userInsertedToDatabase(school);
                        else mCallbacks.userInsertedToDatabase((School) null);
                    }
                });
    }

    @Override
    public void putNewUserInDb(final Users user) {
        reference.child(FirebaseConstants.NORMAL_USERS_NODE)
                .child(user.getId())
                .child(FirebaseConstants.USER_DETAIL_NODE)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            mCallbacks.userInsertedToDatabase(user);
                        else mCallbacks.userInsertedToDatabase((Users) null);
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mCallbacks.login(true,
                                    task.getResult() != null ? task.getResult().getUser() : null);
                        } else {
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.getLocalizedMessage());
                                }
                            });
                            mCallbacks.login(false, null);

                        }
                    }
                });
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        mCallbacks.loggedOut();
    }

    @Override
    public void deleteAccount(Users users) {
//        mAuth.getCurrentUser().delete();
    }

    @Override
    public void deleteAccount(School school) {

    }
}
