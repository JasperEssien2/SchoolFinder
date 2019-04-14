package com.example.android.schoolfinder.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.schoolfinder.BuildConfig;
import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.BaseAuthentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

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
                            Log.e(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private DatabaseReference mSchoolRef;

    @Override
    public void putNewUserInDb(final School school) {
        final DatabaseReference schoolRef = reference.child(FirebaseConstants.SCHOOLS_USERS_NODE);
        mSchoolRef = schoolRef;
        final Map<String, Object> countryMap = new HashMap<>();
        countryMap.put(school.getId(), true);
        schoolRef
//                .child(school.getCountry())
//                .child(school.getState_region())
                .child(school.getId())
                .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                .setValue(school)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> map = null;
                            mSchoolRef.child(FirebaseConstants.COUNTRIES)
                                    .child(school.getCountry())
                                    .updateChildren(countryMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Map<String, Object> stateMap = new HashMap<>();
                                            stateMap.put(school.getId(), true);
                                            mSchoolRef.child(FirebaseConstants.COUNTRIES)
                                                    .child(school.getCountry())
                                                    .child(school.getState_region())
                                                    .updateChildren(stateMap);
                                        }
                                    });

                            DatabaseReference categoryRef =
                                    reference.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                                            .child(FirebaseConstants.CATEGORIES_NODE);
                            for (String s : school.getSchoolCategory()) {
                                map = new HashMap<>();
                                map.put(school.getId(), true);
                                categoryRef.child(s).updateChildren(map);
                            }
                            mCallbacks.userInsertedToDatabase(school);

                        } else mCallbacks.userInsertedToDatabase((School) null);
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

    /**
     * This method updates the user's token in the database each time the device token changes , this
     * token is to be used for the notification services
     */
    public void updateToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.e(TAG, "updateToken() ----------------------- " + token);
                        reference
                                .child(BuildConfig.FLAVOR.equals("normalUsers") ? FirebaseConstants.NORMAL_USERS_NODE
                                        : FirebaseConstants.SCHOOLS_USERS_NODE)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(FirebaseConstants.TOKEN)
                                .setValue(token);
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);

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
    public void getUserDetail(String id, boolean isSchool) {
        if (isSchool) {
            reference.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                    .child(id)
                    .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mCallbacks.userGotten(dataSnapshot.getValue(School.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            reference.child(FirebaseConstants.NORMAL_USERS_NODE)
                    .child(id)
                    .child(FirebaseConstants.USER_DETAIL_NODE)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mCallbacks.userGotten(dataSnapshot.getValue(Users.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    /**
     * This the method is used to change the user's password
     *
     * @param oldEmail the old email
     * @param newEmail the new email
     * @param password password
     */
    public void changeEmail(String oldEmail, final String newEmail, String password) {
        FirebaseUser user = FirebaseAuth.getInstance()
                .getCurrentUser();

        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(oldEmail, password);
        if (user == null) return;

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User email address updated.");
                                            mCallbacks.accountUpdated(true, true, newEmail);
                                        } else mCallbacks.accountUpdated(true, false, null);
                                    }
                                });
                        //----------------------------------------------------------\\
                    }
                });
    }

    /**
     * This method executes task to help change the user's password
     * @param email of the user
     * @param oldPassword old password
     * @param newPassword new password
     */
    public void updatePassword(String email, String oldPassword, final String newPassword) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPassword);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated");
                                        mCallbacks.accountUpdated(false, true, null);
                                    } else {
                                        Log.d(TAG, "Error password not updated");
                                        mCallbacks.accountUpdated(false, false, null);
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
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
