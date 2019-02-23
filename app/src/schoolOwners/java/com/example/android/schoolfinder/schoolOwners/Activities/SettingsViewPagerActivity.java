package com.example.android.schoolfinder.schoolOwners.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.DialogFragments.DialogFragmentImagePreview;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.schoolOwners.Adapters.SchoolSettingsPagerAdapter;
import com.example.android.schoolfinder.schoolOwners.interfaces.SchoolSettingsViewPagerCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SettingsViewPagerActivity extends AppCompatActivity implements AuthenticationCallbacks,
        SchoolSettingsViewPagerCallback, MediaStorageCallback {

    private static final int SELECT_PHOTO_DP = 355;
    private ActivitySettingsViewPagerBinding settingsViewPagerBinding;
    private School school;
    private Authentication authentication;
    private static final String TAG = SettingsViewPagerActivity.class.getSimpleName();
    private static final int SELECT_PHOTO = 358;
    private DialogFragmentImagePreview imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewPagerBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings_view_pager);
        setSupportActionBar(settingsViewPagerBinding.toolbar);
        authentication = new Authentication(this);
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        if(school == null) {
            school = getSchool();
            setUpViewWithData(school);
        }

        settingsViewPagerBinding.settingsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                settingsViewPagerBinding.settingsViewpager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        showLogoHideOwnerPic();
                        break;
                    case 1:
                        hideLogoShowOwnerPic();
                        break;
                    case 2:
                        showLogoHideOwnerPic();
                        break;
                    case 3:
                        showLogoHideOwnerPic();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        settingsViewPagerBinding.schoolSettingsEditSchoolChangeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "Owner settings image picker button clicked oh");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        settingsViewPagerBinding.schoolOwnerSettingsImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "Owner settings image picker button clicked oh");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO_DP);
            }
        });
        settingsViewPagerBinding.settingsTabs.setupWithViewPager(settingsViewPagerBinding.settingsViewpager);
        settingsViewPagerBinding.settingsViewpager.setAdapter(new SchoolSettingsPagerAdapter(
                getSupportFragmentManager(),this, getBundle(), this));
    }

    private Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstants.SCHOOL_BUNDLE, getSchool());
        return bundle;
    }
    private School getSchool() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(BundleConstants.SCHOOL_BUNDLE)) {
                return bundle.getParcelable(BundleConstants.SCHOOL_BUNDLE);
            }
        }
        return null;
    }

    private void setUpViewWithData(School school){
        if (school.getSchoolLogoImageUrl() != null)
            Picasso.get()
                    .load(school.getSchoolLogoImageUrl())
                    .into(settingsViewPagerBinding.schoolSettingsLogoImgview);
        Picasso.get()
                .load(school.getSchoolOwnerDetails().getProfileImageUrl())
                .centerCrop()
                .into(settingsViewPagerBinding.ownerSettingsImage);
    }

    @Override
    public void hideLogoShowOwnerPic() {
        settingsViewPagerBinding.frameLayout.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolDetailsExpresionCard.setVisibility(View.GONE);
        Log.e(TAG, "hideLogoShowOwnerPic() _______________________");
    }

    @Override
    public void showLogoHideOwnerPic() {
        settingsViewPagerBinding.frameLayout.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolDetailsExpresionCard.setVisibility(View.VISIBLE);
        Log.e(TAG, "showLogoHideOwnerPic() _______________________");
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
        if (null != school) {
            this.school = school;
            setUpViewWithData(school);
            Toast.makeText(this, "Changes Made.", Toast.LENGTH_SHORT).show();
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
        if (imageUrl != null) {
            if (imagePreview != null) {
                imagePreview.dismiss();
                school.getSchoolOwnerDetails().setProfileImageUrl(imageUrl);
                authentication.putNewUserInDb(school);
                Picasso.get()
                        .load(imageUrl)
                        .centerCrop()
                        .into(settingsViewPagerBinding.ownerSettingsImage);
            }
        } else
            Toast.makeText(this, "Error saving image, try again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert) {

    }

    @Override
    public void profileImageDeleted(boolean isSuccessful) {

    }

    @Override
    public void logoStored(String imageUrl) {
        if (imageUrl != null) {
            if (imagePreview != null) {
                imagePreview.dismiss();
                school.setSchoolLogoImageUrl(imageUrl);
                authentication.putNewUserInDb(school);
                Picasso.get()
                        .load(imageUrl)
                        .centerCrop()
                        .into(settingsViewPagerBinding.schoolSettingsLogoImgview);
            }
        } else
            Toast.makeText(this, "Error saving image, try again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void schoolImageAdded(String imageUrl) {

    }

    @Override
    public void postImageAdded(Post post, List<Image> images) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    if(data == null) return;
                    final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                    if (imageUri != null) {
                        imagePreview = DialogFragmentImagePreview
                                .newInstance(this,imageUri, BundleConstants.ACTION_STORE_LOGO);
//                        imagePreview.initMediaStorageCallback(this);
                        imagePreview.show(getSupportFragmentManager(),
                                null);
                    } else
                        Toast.makeText(this, "Error getting image", Toast.LENGTH_SHORT).show();

                }
                break;
            case SELECT_PHOTO_DP:
                if (resultCode == RESULT_OK) {
                    if(data == null) return;
                    final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                    if (imageUri != null) {
                        imagePreview = DialogFragmentImagePreview
                                .newInstance(this, imageUri, BundleConstants.ACTION_STORE_PROFILE_IMAGE);
//                        imagePreview.initMediaStorageCallback(this);
                        imagePreview.show(getSupportFragmentManager(),
                                null);
                    } else
                        Toast.makeText(this, "Error getting image", Toast.LENGTH_SHORT).show();

                }
        }
    }
}
