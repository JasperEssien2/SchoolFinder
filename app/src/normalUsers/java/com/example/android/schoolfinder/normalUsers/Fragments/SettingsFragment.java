package com.example.android.schoolfinder.normalUsers.Fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.DialogFragments.DialogFragmentImagePreview;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.FragmentSettingsBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.normalUsers.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static android.app.Activity.RESULT_OK;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements AuthenticationCallbacks, MediaStorageCallback {

    private static final int SELECT_PHOTO = 445;
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private Authentication authentication = new Authentication(this);
    private MediaStorage mediaStorage = new MediaStorage(this);
    private FragmentSettingsBinding binding;
    private Users mUser;
    private Uri imageUri;
    /**
     * This variable stores how many changes occured when in edit mode
     */
    private int changeCount = 0;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        ((HomeActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Setup");
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        disableAllEditView();
        binding.schoolOwnerSettingsImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu() ----- ");
        inflater.inflate(R.menu.setup_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                if (item.getTitle().equals("edit")) {
                    item.setTitle("done");
                    editButtonClicked();
                } else {
                    item.setTitle("edit");
                    doneButtonClicked();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editButtonClicked() {
        enableAllEditView();


    }

    private void doneButtonClicked() {
        disableAllEditView();
        if (mUser == null) return;

        if (mUser.getName().toLowerCase().equals(binding.settingsName.getText().toString().toLowerCase())) {
            changeCount++;
            mUser.setName(binding.settingsName.getText().toString());
        }
        if (mUser.getContact().toLowerCase().equals(binding.settingsContact.getText().toString().toLowerCase())) {
            changeCount++;
            mUser.setContact(binding.settingsContact.getText().toString());
        }
        if (mUser.getEmail().toLowerCase().equals(binding.settingsEmail.getText().toString().toLowerCase())) {
            changeCount++;
            mUser.setEmail(binding.settingsEmail.getText().toString());
        }
        if (mUser.getLocation().toLowerCase().equals(binding.settingsLocation.getText().toString().toLowerCase())) {
            changeCount++;
            mUser.setLocation(binding.settingsLocation.getText().toString());
        }
        if (changeCount != 0)
            authentication.putNewUserInDb(mUser);
    }

    private void disableAllEditView() {
        binding.settingsContact.setEnabled(false);
        binding.settingsEmail.setEnabled(false);
        binding.settingsLocation.setEnabled(false);
        binding.settingsName.setEnabled(false);
    }

    private void enableAllEditView() {
        binding.settingsContact.setEnabled(true);
        binding.settingsEmail.setEnabled(true);
//        binding.settingsLocation.setEnabled(false);
        binding.settingsName.setEnabled(true);
    }

    private void setUpViewWithData() {
        if (mUser == null) return;
        binding.settingsContact.setText(mUser.getContact() != null ? mUser.getContact() : "");
        binding.settingsEmail.setText(mUser.getEmail() != null ? mUser.getEmail() : "");
        binding.settingsLocation.setText(mUser.getLocation() != null ? mUser.getLocation() : "");
        binding.settingsName.setText(mUser.getName() != null ? mUser.getName() : "");
        if (mUser.getProfileImageUrl() != null && !mUser.getProfileImageUrl().isEmpty())
            new PicassoImageLoader(getActivity(), mUser.getProfileImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, binding.ownerSettingsImage);
    }

    private void imagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        DialogFragmentImagePreview imagePreview = DialogFragmentImagePreview
                                .newInstance(this, imageUri, BundleConstants.ACTION_STORE_PROFILE_IMAGE);
                        imagePreview.initMediaStorageCallback(this);
                        imagePreview.show(getActivity().getSupportFragmentManager(),
                                null);
                    } else
                        Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {

    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {

    }

    @Override
    public void userInsertedToDatabase(Users users) {
        mUser = users;
        setUpViewWithData();
    }

    @Override
    public void userInsertedToDatabase(School school) {

    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {

    }

    @Override
    public void userGotten(Users users) {
        mUser = users;
        setUpViewWithData();
    }

    @Override
    public void profileImageStored(String imageUrl, boolean isSuccesful) {
        if (isSuccesful) {
            mUser.setProfileImageUrl(imageUrl);
            authentication.putNewUserInDb(mUser);
        }
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

    }
}
