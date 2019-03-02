package com.example.android.schoolfinder.Models.ViewModels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.FirebaseHelper.PostQueryLivedata;
import com.example.android.schoolfinder.Models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostViewModels extends ViewModel {

    private static final String TAG = PostViewModels.class.getSimpleName();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();


    private List<String> getAllPostUid(boolean isSchool, String userId) {
        Log.e(TAG, "getAllPostUid() called --------------- ");
        final List<String> ids = new ArrayList<>();
        if (isSchool)
            dbRef = dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE).child(userId).child(FirebaseConstants.POSTS_NODE);
        else
            dbRef = dbRef.child(FirebaseConstants.NORMAL_USERS_NODE).child(userId).child(FirebaseConstants.POSTS_NODE);
        Log.e(TAG, "post dbRef --------------- " + dbRef.toString());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "DataSnapshot for user or school detail node --------------- " + dataSnapshot.getChildren().toString());
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    ids.add(s.getKey());
                    Log.e(TAG, "Datasnapshot key --------------- " + s.getKey());
                }
//                callback.uidAllGotten(ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return ids;
    }

    /**
     * This method returns a livedata observe that helps observe changes in the post main node
     *
     * @param isSchool if its a school user
     * @param ids      id of the user
     * @return livedata of post list
     */
    public LiveData<List<Post>> getPostLiveData(boolean isSchool, String ids) {
        //This is to get the livedata of the main post node
        PostQueryLivedata livedata = new PostQueryLivedata(isSchool, ids);

        //This is to get the livedata of the user or school posts node
//        PostQueryLivedata userPostNodeLivedata = new PostQueryLivedata(true, isSchool, ids);
        LiveData<List<Post>> postLiveData = Transformations
                .map(livedata, new Deserializer(isSchool, ids));
        Log.e(TAG, "getPostLiveData() called ------------------- ");
        return postLiveData;
    }

    /**
     * This method returns a livedata observe that helps observe the post uids node of the user
     *
     * @param isSchool if its a school user or not
     * @param ids      the id f the user
     * @return livedata of the list of post uids that is associated with the user
     */
    public LiveData<List<String>> getUserNodeLiveData(boolean isSchool, String ids) {
        PostQueryLivedata livedata = new PostQueryLivedata(null, isSchool, ids);
        LiveData<List<String>> uidLiveData = Transformations.map(livedata, new UserNodePostDeserializer());
        return uidLiveData;
    }

    /**
     * From the a given list of post uid, this gets all the post
     *
     * @param postUids list of post uids
     * @param callback to notify the activity or fragment when list of post is gotten
     */
    public void getAllPostInUserNode(final List<String> postUids, final AllPostInUserNodeCallback callback) {
        if (postUids == null) return;
        final List<Post> postList = new ArrayList<>();


        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.POSTS_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (String s : postUids) {
                            Log.e(TAG, "datasnapshot ------------ " + dataSnapshot.getRef().toString());
                            Post p = dataSnapshot.child(s).getValue(Post.class);
                            Log.e(TAG, "dataSnapshot.child(s) --" + dataSnapshot.child(s).toString());
                            if (null != p) {

                                postList.add(p);
                            }
                        }
                        callback.postGotten(postList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /**
     * A callback to notify that post list is gotten wherever it is called
     */
    public interface AllPostInUserNodeCallback {
        void postGotten(List<Post> posts);
    }

    private class UserNodePostDeserializer implements Function<DataSnapshot, List<String>> {

        @Override
        public List<String> apply(DataSnapshot input) {
            List<String> postUids = new ArrayList<>();
            for (DataSnapshot s : input.getChildren()) {
                postUids.add(s.getKey());
            }
            return postUids;
        }
    }

    private class Deserializer implements Function<DataSnapshot, List<Post>> {
        private final boolean isSchool;
        private final String uid;
        private List<Post> postList = new ArrayList<>();
        private List<String> idList = new ArrayList<>();

        public Deserializer(boolean isSchool, String uid) {
            this.isSchool = isSchool;
            this.uid = uid;

        }

//        @Override
//        public void uidAllGotten(List<String> idList) {
//            this.idList = idList;
//        }

        @Override
        public List<Post> apply(DataSnapshot input) {
            Log.e(TAG, "Deserializer apply() called ------------------- ");
//            getAllPostUid(isSchool, uid, this);
            postList.clear();

            for (DataSnapshot s : input.getChildren()) {
                Post p = s.getValue(Post.class);
                if (null != p)
                    postList.add(p);
            }
//            for (String id : idList) {
//                Log.e(TAG, "getAllPostUid called ------------------- isSchool: " + isSchool +
//                        "Uid " + id);
//                postList.add(input.child(id).getValue(Post.class));
//            }
            return postList;
        }
    }
}
