package com.example.android.schoolfinder.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.schoolfinder.BuildConfig;
import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.notifications.ApiClient;
import com.example.android.schoolfinder.notifications.ApiInterface;
import com.example.android.schoolfinder.notifications.Config;
import com.example.android.schoolfinder.notifications.RequestNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            School school = dataSnapshot.getValue(School.class);
                            if (school != null && school.getFollowers() != null)
                                Log.e(TAG, "Followers uid ------ " + school.getFollowers().keySet());
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
        final String key = dbRef.push().getKey();
        post.setUid(key);
        post.setTimeStamp(ServerValue.TIMESTAMP);

        final Map<String, Object> childUpdates = new HashMap<>();
        //Add it to the post node
        childUpdates.put(FirebaseConstants.POSTS_NODE + "/" + key, post);
        //Add it to the school's post node, containing list of uids of post the school has posted
        childUpdates.put(FirebaseConstants.SCHOOLS_USERS_NODE + "/" + FirebaseAuth.getInstance()
                .getCurrentUser().getUid() + "/" + FirebaseConstants.POSTS_NODE + "/" + key, true);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            School school = dataSnapshot.getValue(School.class);
                            if (school != null && school.getFollowers() != null) {
                                Log.e(TAG, "Followers uid ------ " + school.getFollowers().keySet());
                                uids = new ArrayList<>(school.getFollowers().keySet());
                            }
                            for (String s : uids) {
                                childUpdates.put(FirebaseConstants.NORMAL_USERS_NODE + "/" + s + "/" + FirebaseConstants.POSTS_NODE + "/" + key, true);
                                Log.e(TAG, "" + FirebaseConstants.NORMAL_USERS_NODE + "/" + s + "/" + FirebaseConstants.POSTS_NODE + "/" + key);
                            }
                            MediaStorage mediaStorage = new MediaStorage(new MediaStorageCallback() {
                                @Override
                                public void profileImageStored(String imageUrl, boolean isSuccesful) {

                                }

                                @Override
                                public void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert) {

                                }

                                @Override
                                public void profileImageDeleted(boolean isSuccessful) {

                                }

                                @Override
                                public void logoStored(String imageUrl) {

                                }

                                @Override
                                public void schoolImageAdded(String imageUrl, String tag) {

                                }

                                @Override
                                public void schoolImageAdded(List<Image> images, boolean isSuccessful) {

                                }

                                @Override
                                public void postImageAdded(final Post post, boolean isSuccessful) {
                                    if (isSuccessful) {
                                        dbRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null)
                                                    mCallback.post(post, true);
                                                else mCallback.post(post, false);
                                            }
                                        });
                                    } else {
                                        mCallback.post(post, false);
                                    }
                                }
                            });
                            if (post.getImageList() != null && !post.getImageList().isEmpty())
                                mediaStorage.addPostImages(post, post.getImageList());

                            else {
                                dbRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError != null) mCallback.post(post, true);
                                        else mCallback.post(post, false);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
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
     * @param followButton
     * @param school       instance of school being followed or unfollowed
     * @param userId       the id of the user
     */
    public void schoolFollowersAction(final TextView textView, final View followButton, final School school, final String userId) {
        if (school.getFollowers() == null)
            school.setFollowers(new HashMap<String, Boolean>());
        if (school.getFollowers().containsKey(userId)) {
            // Unstar the post and remove self from stars
            school.setFollowersCount(school.getFollowersCount() - 1);
            school.getFollowers().remove(userId);
            isFollowing = false;
        } else {
            // Star the post and add self to stars
            school.setFollowersCount(school.getFollowersCount() + 1);
            school.getFollowers().put(userId, true);
            isFollowing = true;

        }
        if (followButton instanceof FloatingActionButton)
            ((FloatingActionButton) followButton).setImageResource(isFollowing ? R.drawable.ic_follow : R.drawable.ic_follow);
        else
            followButton.setVisibility(isFollowing ? View.VISIBLE : View.GONE);
        textView.setText(String.valueOf(school.getFollowersCount()));
//                        followButton.setImageDrawable(isFollowing ? R.drawable.foll);
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);


                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }
                        Log.e(TAG, "mutable data -- " + school1.toString());
                        if (school1.getFollowers() == null)
                            school1.setFollowers(new HashMap<String, Boolean>());
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
                        school.setFollowers(school1.getFollowers());
                        school.setFollowersCount(school1.getFollowersCount());
                        // Set value and report transaction success
                        mutableData.setValue(school);
                        return Transaction.success(mutableData);
//                        return null;
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        Log.d(TAG, "following:onComplete:" + databaseError);
                        if (dataSnapshot != null) {
                            School school1 = dataSnapshot.getValue(School.class);
                            if (school1 != null)
                                textView.setText(String.valueOf(school1.getFollowersCount()));
                        }
//                        followButton.setImageDrawable(isFollowing ? R.drawable.foll);
                        if (databaseError == null && isFollowing) {
                            transferAllSchoolPostWhenUserFollows(school, userId);
                            mCallback.following(school, true);
                            sendNotification0(school, "follow");
                        } else if (databaseError == null && !isFollowing) {
                            removeAllSchoolPostWhenUserUnFollows(school, userId);
                            mCallback.following(school, false);
                        }
                    }
                });
    }

//    TODO: Make sure to make the expression button as radio buttons, only 1 expression can be selected at a time

    /**
     * When a user clicks on the impressed smilley to express the impression the school gave
     * this method is called
     *
     * @param school the instance of the school
     * @param userId the id of the user
     */
    public void schoolImpressedAction(final TextView textView, final View impressedButton, final School school, final String userId) {
        boolean isImpressed = false;

        if (school.getImpressedExpressions() == null)
            school.setImpressedExpressions(new HashMap<String, Boolean>());
        if (school.getImpressedExpressions().containsKey(userId)) {
            // Unstar the post and remove self from stars
            school.setImpressedExpressionCount(school.getImpressedExpressionCount() - 1);
            school.getImpressedExpressions().remove(userId);
        } else {
            // Star the post and add self to stars
            school.setImpressedExpressionCount(school.getImpressedExpressionCount() + 1);
            school.getImpressedExpressions().put(userId, true);

            isImpressed = true;
        }
        if (impressedButton instanceof FloatingActionButton)
            ((FloatingActionButton) impressedButton).setImageResource(isImpressed ? R.drawable.ic_smile : R.drawable.ic_smile_deactivated);
        else
            impressedButton.setVisibility(isImpressed ? View.VISIBLE : View.GONE);
        textView.setText(String.valueOf(school.getImpressedExpressionCount()));
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);
                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }

//                        boolean isImpressed = false;
                        if (school1.getImpressedExpressions() == null)
                            school1.setImpressedExpressions(new HashMap<String, Boolean>());

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
    public void schoolNotImpressedAction(final TextView textView, final View notImpressedButton,
                                         final School school, final String userId) {
        //TODO: Add it to the user impressed school node;

        if (school.getNotImpressedExpressions() == null)
            school.setNotImpressedExpressions(new HashMap<String, Boolean>());
        boolean notImpressed = false;

        if (school.getNotImpressedExpressions().containsKey(userId)) {
            // Unstar the post and remove self from stars
            school.setNotImpressedExpressionCount(school.getNotImpressedExpressionCount() - 1);
            school.getNotImpressedExpressions().remove(userId);
        } else {
            // Star the post and add self to stars
            school.setNotImpressedExpressionCount(school.getNotImpressedExpressionCount() + 1);
            school.getNotImpressedExpressions().put(userId, true);
            notImpressed = true;
        }
        if (notImpressedButton instanceof FloatingActionButton)
            ((FloatingActionButton) notImpressedButton).setImageResource(notImpressed ? R.drawable.ic_sad__1 : R.drawable.ic_sad__1_deactivated);
        else
            notImpressedButton.setVisibility(notImpressed ? View.VISIBLE : View.GONE);
        textView.setText(String.valueOf(school.getNotImpressedExpressionCount()));

        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);
                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }
                        if (school1.getNotImpressedExpressions() == null)
                            school1.setNotImpressedExpressions(new HashMap<String, Boolean>());
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
//                            notImpressed = true;
                        }
//                        notImpressedButton.setImageResource(notImpressed ? R.drawable.ic_sad__1 : R.drawable.ic_sad__1_deactivated);
//                        textView.setText(String.valueOf(school1.getNotImpressedExpressionCount()));

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
    public void schoolNormalImpressedAction(final TextView textView, final View normalImpressButton, final School school, final String userId) {
        //TODO: Add it to the user impressed school node;
        if (school.getNormalExpressions() == null)
            school.setNormalExpressions(new HashMap<String, Boolean>());
        boolean isNormalImpressed = false;
        if (school.getNormalExpressions().containsKey(userId)) {
            // Unstar the post and remove self from stars
            school.setNormalExpressionCount(school.getNormalExpressionCount() - 1);
            school.getNormalExpressions().remove(userId);
//                            isNormalImpressed = false;
        } else {
            // Star the post and add self to stars
            school.setNormalExpressionCount(school.getNormalExpressionCount() + 1);
            school.getNormalExpressions().put(userId, true);

            isNormalImpressed = true;
        }
        if (normalImpressButton instanceof FloatingActionButton)
            ((FloatingActionButton) normalImpressButton).setImageResource(isNormalImpressed ? R.drawable.ic_neutral : R.drawable.ic_neutral_deactivated);
        else
            normalImpressButton.setVisibility(isNormalImpressed ? View.VISIBLE : View.GONE);
        textView.setText(String.valueOf(school.getNormalExpressionCount()));
        dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(school.getId())
                .child(FirebaseConstants.SCHOOL_DETAIL_NODE)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                        School school1 = school;
                        School school1 = mutableData.getValue(School.class);
                        if (school1 == null) {
                            return Transaction.success(mutableData);
                        }
                        if (school1.getNormalExpressions() == null)
                            school1.setNormalExpressions(new HashMap<String, Boolean>());
                        if (school1.getNormalExpressions().containsKey(userId)) {
                            // Unstar the post and remove self from stars
                            school1.setNormalExpressionCount(school1.getNormalExpressionCount() - 1);
                            school1.getNormalExpressions().remove(userId);
                            dbRef.child(FirebaseConstants.NORMAL_USERS_NODE)
                                    .child(userId)
                                    .child(FirebaseConstants.NORMAL_EXPRESSION_NODE)
                                    .child(school.getId())
                                    .setValue(null);
//                            isNormalImpressed = false;
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

    private void sendNotification0(final School school, final String notifType) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(FirebaseConstants.NORMAL_USERS_NODE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.USER_DETAIL_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users users = dataSnapshot.getValue(Users.class);

                        if (users == null) return;
                        sendNotification(users, school, notifType);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendNotification(final Users users, final School school, final String notifType) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(BuildConfig.FLAVOR.equals("normalUsers") ? FirebaseConstants.NORMAL_USERS_NODE
                        : FirebaseConstants.SCHOOLS_USERS_NODE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.TOKEN)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String token = dataSnapshot.getValue(String.class);
                        Config config = new Config();
                        RequestNotification requestNotification = new RequestNotification();
                        requestNotification.setToken(token);
                        switch (notifType) {
                            case "follow":
                                config.title = "New Follower";
                                config.content = users.getName() + "followed you";
                                config.imageUrl = users.getProfileImageUrl();
                                requestNotification.setSendNotificationModel(config);
                                ApiClient.getClient()
                                        .create(ApiInterface.class)
                                        .sendNotification(requestNotification)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                Log.e(TAG, "notification url ------- " + response.raw().toString());

                                                Log.e(TAG, "token url ------- " + token);
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
