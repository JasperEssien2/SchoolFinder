package com.example.android.schoolfinder.normalUsers.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.schoolfinder.Adapters.PostAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.Models.ViewModels.PostViewModels;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentActivitiesBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesFragment extends Fragment implements AuthenticationCallbacks, MediaStorageCallback,
        FirebaseTransactionCallback {

    private static final String TAG = ActivitiesFragment.class.getSimpleName();
    FirebaseTransactionsAction transactionsAction = new FirebaseTransactionsAction(this);
    private FragmentActivitiesBinding binding;
    private School school;
    private Authentication authentication = new Authentication(this);
    private MediaStorage mediaStorage = new MediaStorage(this);
    private PostAdapter postAdapter;
    private PostViewModels postViewModel;

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        postViewModel = new ViewModelProvider.NewInstanceFactory().create(PostViewModels.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activities, container, false);
        postAdapter = new PostAdapter(getActivity(), false);
        postAdapter.initTransactionsObject(transactionsAction);
        setUpRecyclerView();
        postViewModel.getUserNodeLiveData(false, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .observe(this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(@Nullable List<String> postsUid) {

                        postViewModel.getAllPostInUserNode(postsUid, new PostViewModels.AllPostInUserNodeCallback() {
                            @Override
                            public void postGotten(List<Post> posts) {
                                if (posts != null && !posts.isEmpty()) {
                                    postListNotEmpty();
                                    postAdapter.setPostList(posts);
//                            for (Post pos : posts) {
//                                postAdapter.putPostItem(pos);
//                                Log.e(TAG, "--- post list not empty and its looping right now -- " + pos.toString());
//                            }
                                } else {
                                    postListEmpty();
                                    Log.e(TAG, "--- post list IS EMPTY OR NULL ");
                                }
                            }
                        });

                    }
                });

        return binding.getRoot();
    }

    /**
     * This method sets the activities fragment recycler view
     */
    private void setUpRecyclerView() {
        binding.galleryFragmentRecyclerView.setAdapter(postAdapter);
        binding.galleryFragmentRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    /**
     * This is called by the add post dialog fragment to add the post
     *
     * @param post instance to be added
     */
    public void addPost(Post post) {
        if (post.getImageList() != null && !post.getImageList().isEmpty())
            mediaStorage.addPostImages(post, post.getImageList());
        else transactionsAction.writeNewPost(post);
    }

    private Bundle getBundle() {
        if (null == school) return null;
        if (school.getSchoolLogoImageUrl() == null) return null;
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstants.IMAGE_URL, new Image(null, school.getSchoolLogoImageUrl(), "logo"));
        return bundle;
    }

    private void postListEmpty() {
        binding.galleryFragmentRecyclerView.setVisibility(View.GONE);
        binding.emptyListView.setVisibility(View.VISIBLE);
    }

    private void postListNotEmpty() {
        binding.galleryFragmentRecyclerView.setVisibility(View.VISIBLE);
        binding.emptyListView.setVisibility(View.GONE);
    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {

    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {

    }

    @Override
    public void userInsertedToDatabase(Users users) {

    }

    @Override
    public void userInsertedToDatabase(School school) {

    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {
        if (school != null)
            this.school = school;
        else Log.e(TAG, "Error getting task..");
    }

    @Override
    public void userGotten(Users users) {

    }

    @Override
    public void accountUpdated(boolean isEmail, boolean isSuccessful, String newEmail) {

    }


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
    public void postImageAdded(Post post, boolean isSuccessful) {
        post.setImageList(post.getImageList());
        transactionsAction.writeNewPost(post);
    }

    @Override
    public void post(Post post, boolean isSuccessful) {
        Toast.makeText(getActivity(), isSuccessful ? "Post upload successful" : "Post upload failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void following(School school, boolean isSuccessful) {

    }

    @Override
    public void impressedExpression(School school, boolean isSuccessful) {

    }

    @Override
    public void notImpressedExpression(School school, boolean isSuccessful) {

    }

    @Override
    public void neutralExpression(School school, boolean isSuccessful) {

    }

    @Override
    public void postLike(Post post, boolean isSuccessful) {
        try {
            if (isSuccessful)
                Toast.makeText(getActivity(), "liked", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
