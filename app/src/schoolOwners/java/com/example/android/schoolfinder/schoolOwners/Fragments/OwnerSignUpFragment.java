package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.Validation;
import com.example.android.schoolfinder.databinding.FragmentOwnersSignUpBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.AuthenticationViewPagerCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.app.Activity.RESULT_OK;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerSignUpFragment extends Fragment implements View.OnClickListener, AuthenticationCallbacks,
        MediaStorageCallback {

    private FragmentOwnersSignUpBinding ownersSignUpBinding;
    private Authentication auth = new Authentication(this);
    private AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks;
    private AppCompatEditText mNameE, mContactE, mEmailE, mLocationE, mBiographyE;
    private TextInputLayout mNameL, mContactL, mEmailL, mLocationL, mBiographyL;
    private static final int SELECT_PHOTO = 454;
    private MediaStorage mediaStorage = new MediaStorage(this);
    private Uri imageUri;

    public OwnerSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ownersSignUpBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_owners_sign_up, container, false);
        initFields();
        ownersSignUpBinding.loginButton.setOnClickListener(this);
        ownersSignUpBinding.nextButton.setOnClickListener(this);
        ownersSignUpBinding.ownerSettingsImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker();
            }
        });

        return ownersSignUpBinding.getRoot();
    }

    /**
     * This method is called to open up the image image picker intent
     */
    private void imagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    public void initAuthenticationCallbacks(AuthenticationViewPagerCallbacks authenticationViewPagerCallbacks) {

        this.authenticationViewPagerCallbacks = authenticationViewPagerCallbacks;
    }

    /**
     * This method initializes login fragment fields
     */
    private void initFields() {
        mNameE = ownersSignUpBinding.signupOwnerName;
        mNameL = ownersSignUpBinding.signupOwnerNameInputLayout;
        mContactE = ownersSignUpBinding.signupOwnerContact;
        mContactL = ownersSignUpBinding.signupOwnerContactInputLayout;
        mEmailE = ownersSignUpBinding.signupOwnerEmail;
        mEmailL = ownersSignUpBinding.signupOwnerEmailInputLayout;
        mLocationE = ownersSignUpBinding.signupOwnerLocation;
        mLocationL = ownersSignUpBinding.signupOwnerLocationInputLayout;
        mBiographyE = ownersSignUpBinding.signupOwnerBiography;
        mBiographyL = ownersSignUpBinding.signupOwnerBiographyInputLayout;
    }

    /**
     * Gets the text from the edittext
     *
     * @param editText required
     * @return String of text
     */
    private String getTextFromEditText(AppCompatEditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * Sets fields error null
     */
    private void setAuthFieldErrorNull() {
        mNameL.setError(null);
        mContactL.setError(null);
        mEmailL.setError(null);
        mLocationL.setError(null);
        mBiographyL.setError(null);
    }


    /**
     * This method validates the fields
     *
     * @return the number of errors
     */
    private int validateFields() {
        int errorCount = 0;
        if (!Validation.validateFields(getTextFromEditText(mNameE))) {
            mNameL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mContactE))) {
            mContactL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateEmail(getTextFromEditText(mEmailE))) {
            mEmailL.setError(Validation.EMAIL_NOT_VALID);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mLocationE))) {
            mLocationL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }

        if (!Validation.validateFields(getTextFromEditText(mBiographyE))) {
            mBiographyL.setError(Validation.EMPTY_FIELD);
            errorCount++;
        }


        return errorCount;
    }


    /**
     * Initializes a user object
     *
     * @return user
     */
    private Users getUser() {
        Users users = new Users();
        users.setName(getTextFromEditText(mNameE));
        users.setContact(getTextFromEditText(mContactE));
        users.setEmail(getTextFromEditText(mEmailE));
        users.setLocation(getTextFromEditText(mLocationE));
        users.setBiography(getTextFromEditText(mBiographyE));
        users.setProfileImageUrl(imageUri.toString());
        return users;
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                setAuthFieldErrorNull();
                if (validateFields() == 0) {
                    authenticationViewPagerCallbacks.nextButtonCLicked(getUser());
                }
                break;
            case R.id.login_button:
                authenticationViewPagerCallbacks.loginButtonClicked(false, null, null);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        Picasso
                                .get()
                                .load(imageUri)
                                .into(ownersSignUpBinding.ownerSettingsImage);
                    } else
                        Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}
