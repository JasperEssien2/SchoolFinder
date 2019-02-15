package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.DialogFragments.DialogFragmentImagePreview;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentOwnerSettingsBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerSettingsFragment extends Fragment implements AuthenticationCallbacks,
        View.OnClickListener, View.OnFocusChangeListener, MediaStorageCallback {

    private static final int SELECT_PHOTO = 454;
    private static final String TAG = OwnerSettingsFragment.class.getSimpleName();
    private FragmentOwnerSettingsBinding ownerSettingsBinding;
    private School school;
    private Authentication authentication;
    private MediaStorage mediaStorage;

    public OwnerSettingsFragment() {
        // Required empty public constructor
    }

    private boolean isBiographyEditMode, isNameEditMode, isContactEditMode,
            isEmailEditMode;
    private DialogFragmentImagePreview imagePreview;

    public static OwnerSettingsFragment newInstance(Bundle bundle) {
        OwnerSettingsFragment ownerSettingsFragment = new OwnerSettingsFragment();
        ownerSettingsFragment.setArguments(bundle);
        return ownerSettingsFragment;
    }

    @Override
    public void onStart() {
        authentication = new Authentication(this);
        mediaStorage = new MediaStorage(this);

        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ownerSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_owner_settings, container, false);
//        if (getArguments() != null && getArguments().containsKey(BundleConstants.SCHOOL_BUNDLE)) ;
        disableAllEditTextFields();
        if (school != null)
            setUpViewWithData(school);
        ownerSettingsBinding.ownersBiographyEditButton.setOnClickListener(this);
        ownerSettingsBinding.ownersEmailEditButton.setOnClickListener(this);
        ownerSettingsBinding.ownersContactEditButton.setOnClickListener(this);
        ownerSettingsBinding.ownersNameEditButton.setOnClickListener(this);
//        ownerSettingsBinding.schoolOwnerSettingsImagePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "Owner settings image picker button clicked oh");
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//            }
//        });

        return ownerSettingsBinding.getRoot();
    }

    private void setUpViewWithData(School school) {
        Users owner = school.getSchoolOwnerDetails();
        if (owner == null) return;
        ownerSettingsBinding.settingsSchoolOwnerBiography.setText(owner.getBiography());
        ownerSettingsBinding.settingsSchoolOwnerName.setText(owner.getName());
        ownerSettingsBinding.settingsSchoolOwnerContact.setText(owner.getContact());
        ownerSettingsBinding.settingsSchoolOwnerEmail.setText(owner.getEmail());
        ownerSettingsBinding.settingsSchoolOwnerLocation.setText(owner.getLocation());
//        Picasso.get()
//                .load(owner.getProfileImageUrl())
//                .into(ownerSettingsBinding.ownerSettingsImage);

    }

    private void editFieldButtonClicked(AppCompatEditText editText) {
        editText.setEnabled(!editText.isEnabled());
    }

    private void disableAllEditTextFields() {
        ownerSettingsBinding.settingsSchoolOwnerBiography.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerName.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerContact.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerEmail.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerLocation.setEnabled(false);
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
        if (school != null) {
            this.school = school;
            setUpViewWithData(school);
            Toast.makeText(getActivity(), "Changes Made.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {
        this.school = school;
        setUpViewWithData(school);
    }

    @Override
    public void userGotten(Users users) {

    }

    @Override
    public void profileImageStored(String imageUrl, boolean isSuccesful) {
//        if (imageUrl != null) {
//            if (imagePreview != null) {
//                imagePreview.dismiss();
//                school.getSchoolOwnerDetails().setProfileImageUrl(imageUrl);
//                authentication.putNewUserInDb(school);
//                Picasso.get()
//                        .load(imageUrl)
//                        .into(ownerSettingsBinding.ownerSettingsImage);
//            }
//        } else
//            Toast.makeText(getActivity(), "Error saving image, try again!", Toast.LENGTH_SHORT).show();
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
    public void schoolImageAdded(String imageUrl) {

    }

    private void setEditButtonIcon(boolean isInEditMode,
                                   AppCompatImageButton imageButton) {

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                Color.GRAY,
                Color.GRAY,
                Color.GRAY,
                Color.GRAY
        };

        ColorStateList colorStateList = new ColorStateList(
                states, colors);
        if (!isInEditMode) {
            imageButton.setImageResource(R.drawable.ic_edit_white);
        } else imageButton.setImageResource(R.drawable.ic_check_white_24dp);
//        imageButton.setSupportImageTintList(colorStateList);

    }

    private void updateUser(School school) {
        authentication.putNewUserInDb(school);
    }

    private void onFocusChangeAction(boolean isFocused, EditText editText, String fieldName) {
        School school = this.school;
        Users ownerDetails = school.getSchoolOwnerDetails();
        String newText = editText.getText().toString();

        if (!isFocused) {
            editText.setEnabled(false);
//            editText.setFocusable();
            switch (fieldName) {
                case "name":
                    ownerDetails.setName(newText);
//                    setEditButtonIcon(false, ownerSettingsBinding.ownersNameEditButton);
                    break;
                case "biography":
                    ownerDetails.setBiography(newText);
//                    setEditButtonIcon(false, ownerSettingsBinding.ownersBiographyEditButton);
                    break;
                case "contact":
                    ownerDetails.setContact(newText);
                    setEditButtonIcon(false, ownerSettingsBinding.ownersContactEditButton);
                    break;
                case "email":
                    ownerDetails.setEmail(newText);
//                    setEditButtonIcon(false, ownerSettingsBinding.ownersEmailEditButton);
                    break;
            }
            if (ownerDetails != null)
                school.setSchoolOwnerDetails(ownerDetails);
            updateUser(school);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.owners_biography_edit_button:
                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerBiography);
                isBiographyEditMode = !isBiographyEditMode;
                if (isBiographyEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerBiography.requestFocus();
                setEditButtonIcon(isBiographyEditMode, ownerSettingsBinding.ownersBiographyEditButton);
                break;
            case R.id.owners_name_edit_button:
                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerName);
                isNameEditMode = !isNameEditMode;
                if (isNameEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerName.requestFocus();
                setEditButtonIcon(isNameEditMode, ownerSettingsBinding.ownersNameEditButton);
                break;
            case R.id.owners_contact_edit_button:
                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerContact);
                isContactEditMode = !isContactEditMode;
                if (isContactEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerContact.requestFocus();
                setEditButtonIcon(isContactEditMode, ownerSettingsBinding.ownersContactEditButton);
                break;
            case R.id.owners_email_edit_button:
                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerEmail);
                isEmailEditMode = !isEmailEditMode;
                if (isEmailEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerEmail.requestFocus();
                setEditButtonIcon(isEmailEditMode, ownerSettingsBinding.ownersEmailEditButton);
                break;
            case R.id.owner_settings_image:
                Log.e(TAG, "Owner settings image picker button clicked oh");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;

        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == ownerSettingsBinding.ownersBiographyEditButton.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerBiography,
                    "biography");
        } else if (view.getId() == ownerSettingsBinding.ownersNameEditButton.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerName,
                    "name");
        } else if (view.getId() == ownerSettingsBinding.ownersContactEditButton.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerContact,
                    "contact");
        } else if (view.getId() == ownerSettingsBinding.ownersEmailEditButton.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerEmail,
                    "email");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                    if (imageUri != null) {
                        imagePreview = DialogFragmentImagePreview
                                .newInstance(this, imageUri, BundleConstants.ACTION_STORE_PROFILE_IMAGE);
                        imagePreview.show(getActivity().getSupportFragmentManager(),
                                null);
                    } else
                        Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();

                }
        }
    }
}
