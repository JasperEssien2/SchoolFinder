package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.content.ClipData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Adapters.PostAdapter;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentSchoolPhotosBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.schoolOwners.Activities.SettingsViewPagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolPhotosFragment extends Fragment implements AuthenticationCallbacks, MediaStorageCallback {

    private static final int REQUEST_CODE_IMG = 56;
    private MediaStorage mediaStorage = new MediaStorage(this);
    private Authentication authentication = new Authentication(this);
    private School school;
    private FragmentSchoolPhotosBinding schoolPhotosBinding;
    private PostAdapter.PostImagesAdapter adapter;

    public SchoolPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        schoolPhotosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_photos, container, false);
        if (school != null && school.getSchoolImages() != null)
            adapter = new PostAdapter.PostImagesAdapter(school.getSchoolImages(), getActivity(), true);
        else
            adapter = new PostAdapter.PostImagesAdapter(new ArrayList<Image>(), getActivity(), true);
        if (getActivity() instanceof SettingsViewPagerActivity) {
            ((SettingsViewPagerActivity) getActivity())
                    .initFabCallback(new SettingsViewPagerActivity.AddImagesFabClicked() {
                        @Override
                        public void fabClicked() {
                            Intent intent = new Intent();
                            intent.setType("image/*");

                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_IMG);
                        }
                    });
        }
        setUpGalleryRecyclerView();
        return schoolPhotosBinding.getRoot();
    }

    private void setUpGalleryRecyclerView() {
        schoolPhotosBinding.recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        schoolPhotosBinding.recyclerViewPhotos.setAdapter(adapter);
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
        this.school = school;
        if (school == null) return;
        if (school.getSchoolImages() == null) return;
        adapter.setImages(school.getSchoolImages());
    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {
        this.school = school;
        if (school == null) return;
        if (school.getSchoolImages() == null) return;
        adapter.setImages(school.getSchoolImages());
    }

    @Override
    public void userGotten(Users users) {

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
        if (isSuccessful && school.getSchoolImages() != null)
            school.getSchoolImages().addAll(images);
        authentication.putNewUserInDb(school);
    }

    @Override
    public void postImageAdded(Post post, boolean isSuccessful) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_IMG:
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getClipData() != null) {
                        List<Image> images = new ArrayList<>();

                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            ClipData.Item item = data.getClipData().getItemAt(i);
                            images.add(new Image(FirebaseDatabase.getInstance().getReference().push().getKey(),
                                    item.getUri().toString(), ""));
                        }

                        mediaStorage.addSchoolImages(school, images);
                    }
                }
                break;

        }

    }
}
