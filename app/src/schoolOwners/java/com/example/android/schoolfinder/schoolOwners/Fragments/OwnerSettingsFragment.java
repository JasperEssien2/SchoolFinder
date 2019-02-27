package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentOwnerSettingsBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        authentication = new Authentication(this);
        mediaStorage = new MediaStorage(this);

        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ownerSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_owner_settings, container, false);
//        if (getArguments() != null && getArguments().containsKey(BundleConstants.SCHOOL_BUNDLE)) ;
        disableAllEditTextFields();
        clearAllFieldFocus();
        if (school != null)
            setUpViewWithData(school);
        ownerSettingsBinding.ownersBiographyEditButton.setOnClickListener(this);
        ownerSettingsBinding.ownersEmailEditButton.setOnClickListener(this);
        ownerSettingsBinding.ownersContactEditButton.setOnClickListener(this);
        ownerSettingsBinding.ownersNameEditButton.setOnClickListener(this);
        ownerSettingsBinding.settingsSchoolOwnerBiography.setOnFocusChangeListener(this);
        ownerSettingsBinding.settingsSchoolOwnerName.setOnFocusChangeListener(this);
        ownerSettingsBinding.settingsSchoolOwnerContact.setOnFocusChangeListener(this);
        ownerSettingsBinding.settingsSchoolOwnerEmail.setOnFocusChangeListener(this);
        ownerSettingsBinding.settingsSchoolOwnerLocation.setOnFocusChangeListener(this);

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

    /**
     * This disables all the edittext fields, and it is called
     * when onCreate is called
     */
    private void disableAllEditTextFields() {
        ownerSettingsBinding.settingsSchoolOwnerBiography.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerName.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerContact.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerEmail.setEnabled(false);
        ownerSettingsBinding.settingsSchoolOwnerLocation.setEnabled(false);
    }

    /**
     * This clears all the edit text field focus
     * and it is called when onCreate is called
     */
    private void clearAllFieldFocus() {
        ownerSettingsBinding.settingsSchoolOwnerBiography.clearFocus();
        ownerSettingsBinding.settingsSchoolOwnerName.clearFocus();
        ownerSettingsBinding.settingsSchoolOwnerContact.clearFocus();
        ownerSettingsBinding.settingsSchoolOwnerEmail.clearFocus();
        ownerSettingsBinding.settingsSchoolOwnerLocation.clearFocus();
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
    public void schoolImageAdded(String imageUrl, String tag) {

    }

    @Override
    public void postImageAdded(Post post, List<Image> images) {

    }

    /**
     * This sets the button for the edittext depending on whether the edittxt is
     * in edit mode
     *
     * @param isInEditMode a boolean for controlling the button to be used
     * @param imageButton  the button
     */
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

    /**
     * This method is used to insert/replace the previous data with the new changed data
     *
     * @param school
     */
    private void updateUser(School school) {
        Log.e(TAG, "Update user called --- ");
        authentication.putNewUserInDb(school);
    }

    /**
     * This method is called to perform actions on the edittext based on its focused state
     *
     * @param isFocused boolean indicating its focus state
     * @param editText  the editext to perform the action
     * @param fieldName to kno which field is calling the method on its changed focus states
     */
    private void onFocusChangeAction(boolean isFocused, EditText editText, String fieldName) {
        School school = this.school;
        Users ownerDetails = school.getSchoolOwnerDetails();
        String newText = editText.getText().toString();
        //This variable is used to know if the previous text chaned, if yes, then it updates the new data
        boolean textChanged = false;

        Log.e(TAG, "IsFocused --- " + isFocused);
//        if (!isFocused) {
//            editText.setFocusable();
        if (isFocused) editText.setEnabled(true);
        else editText.setEnabled(false);
        switch (fieldName) {
            case "name":
                if (!isFocused) {
                    if (null != ownerDetails.getName() && !ownerDetails.getName().equals(newText)) {
                        ownerDetails.setName(newText);
                        textChanged = true;
                    }
                    setEditButtonIcon(false, ownerSettingsBinding.ownersNameEditButton);
                    editText.setEnabled(false);
                } else setEditButtonIcon(true, ownerSettingsBinding.ownersNameEditButton);
                break;
            case "biography":
                if (!isFocused) {
                    if (null != ownerDetails.getBiography() && !ownerDetails.getBiography().equals(newText)) {
                        ownerDetails.setBiography(newText);
                        textChanged = true;
                    }
                    setEditButtonIcon(false, ownerSettingsBinding.ownersBiographyEditButton);
                    editText.setEnabled(false);
                } else setEditButtonIcon(true, ownerSettingsBinding.ownersBiographyEditButton);
                break;
            case "contact":
                if (!isFocused) {
                    if (null != ownerDetails.getContact() && !ownerDetails.getContact().equals(newText)) {
                        ownerDetails.setContact(newText);
                        textChanged = true;
                    }
                    setEditButtonIcon(false, ownerSettingsBinding.ownersContactEditButton);
                    editText.setEnabled(false);
                } else setEditButtonIcon(true, ownerSettingsBinding.ownersContactEditButton);
                break;
            case "email":
                if (!isFocused) {
                    if (null != ownerDetails.getEmail() && !ownerDetails.getEmail().equals(newText)) {
                        ownerDetails.setEmail(newText);
                    }
                    setEditButtonIcon(false, ownerSettingsBinding.ownersEmailEditButton);
                    editText.setEnabled(false);
                } else setEditButtonIcon(true, ownerSettingsBinding.ownersEmailEditButton);
                break;
        }
        if (!isFocused && ownerDetails != null) {
            school.setSchoolOwnerDetails(ownerDetails);
            if (textChanged)
                updateUser(school);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.owners_biography_edit_button:
//                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerBiography);
                isBiographyEditMode = !isBiographyEditMode;
//                setEditButtonIcon(isBiographyEditMode, ownerSettingsBinding.ownersBiographyEditButton);
                if (isBiographyEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerBiography.requestFocus();
                else ownerSettingsBinding.settingsSchoolOwnerBiography.clearFocus();
                break;
            case R.id.owners_name_edit_button:
//                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerName);
                isNameEditMode = !isNameEditMode;
//                setEditButtonIcon(isNameEditMode, ownerSettingsBinding.ownersNameEditButton);
                if (isNameEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerName.requestFocus();
                else ownerSettingsBinding.settingsSchoolOwnerName.clearFocus();
                break;
            case R.id.owners_contact_edit_button:
//                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerContact);
                isContactEditMode = !isContactEditMode;
//                setEditButtonIcon(isContactEditMode, ownerSettingsBinding.ownersContactEditButton);
                if (isContactEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerContact.requestFocus();
                else ownerSettingsBinding.settingsSchoolOwnerContact.clearFocus();
                break;
            case R.id.owners_email_edit_button:
//                editFieldButtonClicked(ownerSettingsBinding.settingsSchoolOwnerEmail);
                isEmailEditMode = !isEmailEditMode;
//                setEditButtonIcon(isEmailEditMode, ownerSettingsBinding.ownersEmailEditButton);
                if (isEmailEditMode)
                    ownerSettingsBinding.settingsSchoolOwnerEmail.requestFocus();
                else ownerSettingsBinding.settingsSchoolOwnerEmail.clearFocus();
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

        if (view.getId() == ownerSettingsBinding.settingsSchoolOwnerBiography.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerBiography,
                    "biography");
//            if (b) setEditButtonIcon(true, ownerSettingsBinding.ownersBiographyEditButton);
//            else setEditButtonIcon(false, ownerSettingsBinding.ownersBiographyEditButton);
            Log.e(TAG, "Biography onFucusChanged called --- ");
        } else if (view.getId() == ownerSettingsBinding.settingsSchoolOwnerName.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerName,
                    "name");
//            if (b) setEditButtonIcon(true, ownerSettingsBinding.ownersNameEditButton);
//            else setEditButtonIcon(false, ownerSettingsBinding.ownersNameEditButton);
        } else if (view.getId() == ownerSettingsBinding.settingsSchoolOwnerContact.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerContact,
                    "contact");
//            if (b) setEditButtonIcon(true, ownerSettingsBinding.ownersContactEditButton);
//            else setEditButtonIcon(false, ownerSettingsBinding.ownersContactEditButton);
        } else if (view.getId() == ownerSettingsBinding.settingsSchoolOwnerEmail.getId()) {
            onFocusChangeAction(b, ownerSettingsBinding.settingsSchoolOwnerEmail,
                    "email");
//            if (b) setEditButtonIcon(true, ownerSettingsBinding.ownersEmailEditButton);
//            else setEditButtonIcon(false, ownerSettingsBinding.ownersEmailEditButton);
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
