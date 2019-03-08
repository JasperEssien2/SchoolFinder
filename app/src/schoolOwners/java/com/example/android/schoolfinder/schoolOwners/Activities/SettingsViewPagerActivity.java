package com.example.android.schoolfinder.schoolOwners.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ActivitySettingsViewPagerBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.schoolOwners.Adapters.SchoolSettingsPagerAdapter;
import com.example.android.schoolfinder.schoolOwners.DialogFragments.AddClassOrCourseDialogFragment;
import com.example.android.schoolfinder.schoolOwners.Fragments.ClassCourseSettingsFragment;
import com.example.android.schoolfinder.schoolOwners.interfaces.SchoolSettingsViewPagerCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SettingsViewPagerActivity extends AppCompatActivity implements AuthenticationCallbacks,
        SchoolSettingsViewPagerCallback, MediaStorageCallback {

    private static final int SELECT_PHOTO_DP = 355;
    private static final int SELECT_BACKGROUND_PHOTO = 555;
    private ActivitySettingsViewPagerBinding settingsViewPagerBinding;
    private School school;
    private Authentication authentication;
    private static final String TAG = SettingsViewPagerActivity.class.getSimpleName();
    private static final int SELECT_PHOTO = 358;
    private DialogFragmentImagePreview imagePreview;
    /**
     * This is initialized to get the current page of the viewpager
     */
    private TabLayout.Tab mTab;
    private SchoolSettingsPagerAdapter viewpagerAdapter;
    private AddImagesFabClicked fabClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        settingsViewPagerBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings_view_pager);
        setSupportActionBar(settingsViewPagerBinding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        authentication = new Authentication(this);
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        if (school == null) {
            school = getSchool();
            setUpViewWithData(school);
        }
        settingsViewPagerBinding.settingsTabs.setupWithViewPager(settingsViewPagerBinding.settingsViewpager);
        viewpagerAdapter = new SchoolSettingsPagerAdapter(
                getSupportFragmentManager(), this, getBundle(), this);
        settingsViewPagerBinding.settingsViewpager.setAdapter(viewpagerAdapter);

        settingsViewPagerBinding.settingsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTab = tab;
                viewpagerAdapter.tabSelected(tab.getPosition());
                settingsViewPagerBinding.settingsViewpager.setCurrentItem(tab.getPosition());

                Log.e(TAG, "Tab position ----- " + tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        showLogoHideOwnerPic();
                        hideFab();
                        break;
                    case 1:
                        hideFab();
                        hideLogoShowOwnerPic();
                        break;
                    case 2:
                        showFab();
                        showLogoHideOwnerPic();
                        break;
                    case 3:
                        showFab();
                        showLogoHideOwnerPic();
                        break;
                    case 4:
                        showFab();
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
        settingsViewPagerBinding.schoolSettingsLogoImgview.setOnClickListener(new View.OnClickListener() {
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

        settingsViewPagerBinding.addCoursesClassesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTab.getPosition() == 2) {
                    if (school != null) {
                        Bundle b = new Bundle();
                        b.putBoolean(BundleConstants.IS_CLASS_SEETTING, true);
                        AddClassOrCourseDialogFragment dialogFragment = AddClassOrCourseDialogFragment.newInstance(b);
                        ClassCourseSettingsFragment fragment =
                                (ClassCourseSettingsFragment) viewpagerAdapter.getRegisteredFragment(2);
//                                        getSupportFragmentManager().findFragmentByTag(
//                                        "android:switcher:" + R.id.settings_viewpager + ":" +
//                                                settingsViewPagerBinding.settingsViewpager.getCurrentItem());
                        dialogFragment.initCallback(null, school);
                        if (fragment != null) {
                            Log.e(TAG, "Fragment found ---- ");
                            dialogFragment.initFragment(fragment);
                            dialogFragment.show(getSupportFragmentManager(), null);
                        } else {
                            Log.e(TAG, "Fragment IS NULL ---- ");
                        }
                    }

                } else if (mTab.getPosition() == 3) {
                    if (school != null) {
                        Bundle b = new Bundle();
                        b.putBoolean(BundleConstants.IS_CLASS_SEETTING, false);
                        AddClassOrCourseDialogFragment dialogFragment = AddClassOrCourseDialogFragment.newInstance(b);
                        ClassCourseSettingsFragment fragment =
                                (ClassCourseSettingsFragment) viewpagerAdapter.getRegisteredFragment(3);
//                                        getSupportFragmentManager().findFragmentByTag(
//                                        "android:switcher:" + R.id.settings_viewpager + ":" +
//                                                settingsViewPagerBinding.settingsViewpager.getCurrentItem());
                        dialogFragment.initCallback(null, school);
                        if (fragment != null) {
                            Log.e(TAG, "Fragment found ---- ");
                            dialogFragment.initFragment(fragment);
                            dialogFragment.show(getSupportFragmentManager(), null);
                        } else {
                            Log.e(TAG, "Fragment IS NULL ---- ");
                        }
                    }
                } else if (mTab.getPosition() == 4) {
                    if (fabClicked != null)
                        fabClicked.fabClicked();
                }
            }
        });
    }

    public void initFabCallback(AddImagesFabClicked fabClicked) {

        this.fabClicked = fabClicked;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_school_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_background_image:
                addBackgroundImageAction();
                return true;
            case R.id.menu_setup_class_with_sheet:
                return true;
            case R.id.menu_setup_course_with_sheet:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when add background image option menu is selected
     */
    private void addBackgroundImageAction() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_BACKGROUND_PHOTO);
    }

    private void setUpViewWithData(School school) {
        if (school.getSchoolLogoImageUrl() != null)
            new PicassoImageLoader(this, school.getSchoolLogoImageUrl(), R.color.colorLightGrey, R.color.colorLightGrey,
                    settingsViewPagerBinding.schoolSettingsLogoImgview);
        if (school.getSchoolImages() != null)
            if (school.getSchoolImages().get(0) != null)
                new PicassoImageLoader(this, school.getSchoolImages().get(0).getImageUrl(), R.color.colorLightGrey, R.color.colorLightGrey,
                        settingsViewPagerBinding.appbarImage);

        if (school.getSchoolOwnerDetails().getProfileImageUrl() != null)
            new PicassoImageLoader(this, school.getSchoolOwnerDetails().getProfileImageUrl(), R.color.colorLightGrey, R.color.colorLightGrey,
                    settingsViewPagerBinding.ownerSettingsImage);
        settingsViewPagerBinding.schoolName.setText(school.getSchoolName() != null ? school.getSchoolName() :
                "");
        settingsViewPagerBinding.schoolAddress.setText(school.getSchoolLocation() != null ? school.getSchoolLocation() :
                "");
    }

    private Bundle getBundle() {
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

    @Override
    public void hideLogoShowOwnerPic() {
        settingsViewPagerBinding.frameLayout.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolAddress.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolName.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolSettingsLogoImgview.setVisibility(View.GONE);
        settingsViewPagerBinding.facebook.setVisibility(View.GONE);
        settingsViewPagerBinding.twitter.setVisibility(View.GONE);
        settingsViewPagerBinding.mail.setVisibility(View.GONE);
//        settingsViewPagerBinding.schoolSettingsEditName.setVisibility(View.GONE);
        Log.e(TAG, "hideLogoShowOwnerPic() _______________________");
    }

    @Override
    public void showLogoHideOwnerPic() {
        settingsViewPagerBinding.frameLayout.setVisibility(View.GONE);
        settingsViewPagerBinding.schoolAddress.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolName.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.schoolSettingsLogoImgview.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.facebook.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.twitter.setVisibility(View.VISIBLE);
        settingsViewPagerBinding.mail.setVisibility(View.VISIBLE);
//        settingsViewPagerBinding.schoolSettingsEditName.setVisibility(View.VISIBLE);
        Log.e(TAG, "showLogoHideOwnerPic() _______________________");
    }

    @Override
    public void schoolImageAdded(String imageUrl, String tag) {
        if (school == null) return;
        if (tag.equals(getString(R.string.SCHOOL_BACKGROUND_IMAGE_TAG))) {
            List<Image> imageList;
            if (school.getSchoolImages() == null) {
                imageList = new ArrayList<>();
                school.setSchoolImages(new ArrayList<Image>());
            } else imageList = school.getSchoolImages();

            Image img = new Image(null, imageUrl, tag);
            if (!(imageList.size() > 0))
                imageList.add(0, img);
            else imageList.set(0, img);
            Log.e(TAG, "images ---- " + school.getSchoolImages().toString());
            school.setSchoolImages(imageList);
            new PicassoImageLoader(this, imageUrl, R.color.colorLightGrey,
                    R.color.colorLightGrey, settingsViewPagerBinding.appbarImage);
            authentication.putNewUserInDb(school);
        }
    }

    private void hideFab() {
        settingsViewPagerBinding.addCoursesClassesFab.setVisibility(View.GONE);
    }

    private void showFab() {
        settingsViewPagerBinding.addCoursesClassesFab.setVisibility(View.VISIBLE);
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
//                        .centerCrop()
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
//                        .centerCrop()
                        .into(settingsViewPagerBinding.schoolSettingsLogoImgview);
            }
        } else
            Toast.makeText(this, "Error saving image, try again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void schoolImageAdded(List<Image> images, boolean isSuccessful) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data == null) return;
                    final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                    if (imageUri != null) {
                        imagePreview = DialogFragmentImagePreview
                                .newInstance(this, imageUri, BundleConstants.ACTION_STORE_LOGO);
//                        imagePreview.initMediaStorageCallback(this);
                        imagePreview.show(getSupportFragmentManager(),
                                null);
                    } else
                        Toast.makeText(this, "Error getting image", Toast.LENGTH_SHORT).show();

                }
                break;
            case SELECT_PHOTO_DP:
                if (resultCode == RESULT_OK) {
                    if (data == null) return;
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
                break;
            case SELECT_BACKGROUND_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data == null) return;
                    final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                    if (imageUri != null) {
                        imagePreview = DialogFragmentImagePreview
                                .newInstance(this, imageUri, BundleConstants.ACTION_STORE_SCHOOL_BACKGROUND_IMAGES);
//                        imagePreview.initMediaStorageCallback(this);
                        imagePreview.show(getSupportFragmentManager(),
                                null);
                    } else
                        Toast.makeText(this, "Error getting image", Toast.LENGTH_SHORT).show();

                }
        }

    }

    @Override
    public void postImageAdded(Post post, boolean isSuccessful) {

    }

    public interface AddImagesFabClicked {
        void fabClicked();
    }
}
