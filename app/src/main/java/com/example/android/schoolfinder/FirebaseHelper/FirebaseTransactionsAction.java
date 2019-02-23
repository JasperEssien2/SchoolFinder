package com.example.android.schoolfinder.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseTransactionsAction {

    private static final String TAG = FirebaseTransactionsAction.class.getSimpleName();
    private FirebaseTransactionCallback mCallback;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private boolean isFollowing = false;
    private List<String> uids = new ArrayList<>();


    public FirebaseTransactionsAction(FirebaseTransactionCallback callback) {

        mCallback = callback;
    }

    private List<String> getAllFollowiersUid() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            School school = dataSnapshot.getValue(School.class);
                            if (school != null && school.getFollowers() != null)
                                uids = new ArrayList<>(school.getFollowers().keySet());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        return uids;
    }

    /**
     * This method is called to write post to the database, it handles the firebase transaction
     * to the done
     *
     * @param post the instance of the post
     */
    public void writeNewPost(final Post post) {
//        DatabaseReference postMainRef = dbRef.child(FirebaseConstants.POSTS_NODE);
//        DatabaseReference dbRefNormal = dbRef
//        TODO: initialize the post timestamp ooh
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        String key = dbRef.push().getKey();
        post.setUid(key);

        Map<String, Object> childUpdates = new HashMap<>();
        //Add it to the post node
        childUpdates.put(FirebaseConstants.POSTS_NODE + "/" + key, post);
        //Add it to the school's post node, containing list of uids of post the school has posted
        childUpdates.put(FirebaseConstants.SCHOOLS_USERS_NODE + "/" + FirebaseAuth.getInstance()
                .getCurrentUser().getUid() + "/" + FirebaseConstants.POSTS_NODE + "/" + key, true);
        for (String s : getAllFollowiersUid()) {
            childUpdates.put(FirebaseConstants.NORMAL_USERS_NODE + "/" + s + "/" + FirebaseConstants.POSTS_NODE + "/" + key, true);
        }

        dbRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) mCallback.post(post, true);
                else mCallback.post(post, false);
            }
        });
    }

    public void likePost(Post post) {

    }

    /**
     * This method is called When a user follows a school, all the ids of post the school has posted will be transfered
     * to the users post node
     *
     * @param school the instance of the school that is being followed
     * @param uid    the uid of the user
     */
    private void transferAllSchoolPostWhenUserFollows(final School school, final String uid) {
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .child(FirebaseConstants.POSTS_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            if (s != null) {
                                dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                        .child(uid)
                                        .child(FirebaseConstants.POSTS_NODE)
                                        .child(s.getKey())
                                        .setValue(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * This method is called When a user unfollows a school, all the ids of post the school has posted will be deleted
     * to the users post node
     *
     * @param school the instance of the school that is being unfollowed
     * @param uid    the uid of the user
     */
    private void removeAllSchoolPostWhenUserUnFollows(final School school, final String uid) {
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .child(FirebaseConstants.POSTS_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            if (s != null) {
                                dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                        .child(uid)
                                        .child(FirebaseConstants.POSTS_NODE)
                                        .child(s.getKey())
                                        .setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * This method is called when a user follows or unfollows a school
     *
     * @param school instance of school being followed or unfollowed
     * @param userId the id of the user
     */
    public void schoolFollowersAction(final School school, final String userId) {
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);
                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }

                        if (school1.getFollowers().containsKey(userId)) {
                            // Unstar the post and remove self from stars
                            school1.setFollowersCount(school1.getFollowersCount() - 1);
                            school1.getFollowers().remove(userId);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.FOLLOWING_NODE)
                                    .child(school.getId())
                                    .setValue(null);
                            isFollowing = false;
                        } else {
                            // Star the post and add self to stars
                            school1.setFollowersCount(school1.getFollowersCount() + 1);
                            school1.getFollowers().put(userId, true);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.FOLLOWING_NODE)
                                    .child(school.getId())
                                    .setValue(true);
                            isFollowing = true;
                        }

                        // Set value and report transaction success
                        mutableData.setValue(school1);
                        return Transaction.success(mutableData);
//                        return null;
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        Log.d(TAG, "following:onComplete:" + databaseError);
                        if (databaseError == null && isFollowing) {
                            transferAllSchoolPostWhenUserFollows(school, userId);
                            mCallback.following(school, true);
                        } else if (databaseError == null && !isFollowing) {
                            removeAllSchoolPostWhenUserUnFollows(school, userId);
                            mCallback.following(school, false);
                        }
                    }
                });
    }

    /**
     * When a user clicks on the impressed smilley to express the impression the school gave
     * this method is called
     *
     * @param school the instance of the school
     * @param userId the id of the user
     */
    public void schoolImpressedAction(final School school, final String userId) {
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);
                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }

                        if (school1.getImpressedExpressions().containsKey(userId)) {
                            // Unstar the post and remove self from stars
                            school1.setImpressedExpressionCount(school1.getImpressedExpressionCount() - 1);
                            school1.getImpressedExpressions().remove(userId);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.IMPRESSED_EXPRESSION_NODE)
                                    .child(school.getId())
                                    .setValue(null);
                        } else {
                            // Star the post and add self to stars
                            school1.setImpressedExpressionCount(school1.getImpressedExpressionCount() + 1);
                            school1.getImpressedExpressions().put(userId, true);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.IMPRESSED_EXPRESSION_NODE)
                                    .child(school.getId())
                                    .setValue(true);
                        }

                        // Set value and report transaction success
                        mutableData.setValue(school1);
                        return Transaction.success(mutableData);
//                        return null;
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        Log.d(TAG, "impressedTransaction:onComplete:" + databaseError);
                        if (databaseError == null) mCallback.impressedExpression(school, true);
                        else mCallback.impressedExpression(school, false);
                    }
                });
    }

    /**
     * When a user clicks on the not impressed smilley to express the impression the school gave
     * this method is called
     *
     * @param school the instance of the school
     * @param userId the id of the user
     */
    public void schoolNotImpressedAction(final School school, final String userId) {
        //TODO: Add it to the user impressed school node;
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);
                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }

                        if (school1.getNotImpressedExpressions().containsKey(userId)) {
                            // Unstar the post and remove self from stars
                            school1.setNotImpressedExpressionCount(school1.getNotImpressedExpressionCount() - 1);
                            school1.getNotImpressedExpressions().remove(userId);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.NOT_IMPRESSED_EXPRESSION_NODE)
                                    .child(school.getId())
                                    .setValue(null);
                        } else {
                            // Star the post and add self to stars
                            school1.setNotImpressedExpressionCount(school1.getNotImpressedExpressionCount() + 1);
                            school1.getNotImpressedExpressions().put(userId, true);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.NOT_IMPRESSED_EXPRESSION_NODE)
                                    .child(school.getId())
                                    .setValue(true);
                        }

                        // Set value and report transaction success
                        mutableData.setValue(school1);
                        return Transaction.success(mutableData);
//                        return null;
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        Log.d(TAG, "notImpressedTransaction:onComplete:" + databaseError);
                        if (databaseError == null) mCallback.notImpressedExpression(school, true);
                        else mCallback.notImpressedExpression(school, false);
                    }
                });
    }

    /**
     * When a user clicks on the normal/neutal impressed smilley to express the impression the school gave
     * this method is called
     *
     * @param school the instance of the school
     * @param userId the id of the user
     */
    public void schoolNormalImpressedAction(final School school, final String userId) {
        //TODO: Add it to the user impressed school node;
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);
                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }

                        if (school1.getNormalExpressions().containsKey(userId)) {
                            // Unstar the post and remove self from stars
                            school1.setNormalExpressionCount(school1.getNormalExpressionCount() - 1);
                            school1.getNormalExpressions().remove(userId);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.NORMAL_EXPRESSION_NODE)
                                    .child(school.getId())
                                    .setValue(null);
                        } else {
                            // Star the post and add self to stars
                            school1.setNormalExpressionCount(school1.getNormalExpressionCount() + 1);
                            school1.getNormalExpressions().put(userId, true);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.NORMAL_EXPRESSION_NODE)
                                    .child(school.getId())
                                    .setValue(true);
                        }

                        // Set value and report transaction success
                        mutableData.setValue(school1);
                        return Transaction.success(mutableData);
//                        return null;
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        Log.d(TAG, "notImpressedTransaction:onComplete:" + databaseError);
                        if (databaseError == null) mCallback.notImpressedExpression(school, true);
                        else mCallback.notImpressedExpression(school, false);
                    }
                });
    }

}
