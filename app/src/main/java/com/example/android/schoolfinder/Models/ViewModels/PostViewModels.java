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

    private List<String> getAllPostUid(boolean isSchool, String userId, final TaskToGetAllUidCallback callback) {
        final List<String> ids = new ArrayList<>();
        if (isSchool)
            dbRef = dbRef.child(FirebaseConstants.SCHOOLS_USERS_NODE).child(userId).child(FirebaseConstants.POSTS_NODE);
        else
            dbRef = dbRef.child(FirebaseConstants.NORMAL_USERS_NODE).child(userId).child(FirebaseConstants.POSTS_NODE);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    ids.add(s.getKey());
                    Log.e(TAG, "Datasnapshot key --------------- " + s.getKey());
                }
                callback.uidAllGotten(ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return ids;
    }


    public LiveData<List<Post>> getPostLiveData(boolean isSchool, String ids) {
        PostQueryLivedata livedata = new PostQueryLivedata();
        LiveData<List<Post>> postLiveData = Transformations
                .map(livedata, new Deserializer(isSchool, ids));
        Log.e(TAG, "getPostLiveData() called ------------------- ");
        return postLiveData;
    }

    private interface TaskToGetAllUidCallback {
        void uidAllGotten(List<String> idList);
    }

    private class Deserializer implements Function<DataSnapshot, List<Post>>, TaskToGetAllUidCallback {
        private final boolean isSchool;
        private final String uid;
        private List<Post> postList = new ArrayList<>();
        private List<String> idList = new ArrayList<>();

        public Deserializer(boolean isSchool, String uid) {

            this.isSchool = isSchool;
            this.uid = uid;
            getAllPostUid(isSchool, uid, this);
        }

        @Override
        public void uidAllGotten(List<String> idList) {
            this.idList = idList;
        }

        @Override
        public List<Post> apply(DataSnapshot input) {
            Log.e(TAG, "Deserializer apply() called ------------------- ");
            postList.clear();

            for (String id : idList) {
                Log.e(TAG, "getAllPostUid called ------------------- isSchool: " + isSchool +
                        "Uid " + id);
                postList.add(input.child(id).getValue(Post.class));
            }
            return postList;
        }
    }
}
