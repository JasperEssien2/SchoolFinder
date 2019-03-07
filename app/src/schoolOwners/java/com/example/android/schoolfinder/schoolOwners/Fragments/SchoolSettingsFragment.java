package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.schoolfinder.Adapters.CertificateAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.DialogFragments.DialogFragmentImagePreview;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.AppLocationService;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.FragmentSchoolSettingsBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.schoolOwners.DialogFragments.AddCertDialogFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolSettingsFragment extends Fragment implements View.OnClickListener,
        View.OnFocusChangeListener, AuthenticationCallbacks, MediaStorageCallback {

    private static final String TAG = SchoolSettingsFragment.class.getSimpleName();
    private static final int SELECT_PHOTO = 458;
    private static final int SELECT_PHOTO_CERT = 459;
    private static final int SELECT_PHOTO_ACHIEVE = 450;

    private FragmentSchoolSettingsBinding schoolSettingsBinding;
    private AppLocationService locationService;
    private School school;
    private Authentication authentication;
    private CertificateAdapter certificateAdapter, achievementAdapter;
    /**
     * This field is used to control the edit button icon
     * if its in edit mode a check icon be used
     * if not in edit mode an edit icon be used
     */
    private boolean isBiographyEditMode, isMottoEditMode, isAchievemnetEditMode,
            isCertificateEditMode, isLocationEditMode;
    private DialogFragmentImagePreview imagePreview;
    private AddCertDialogFragment certDialogFragment;

    public SchoolSettingsFragment() {
        // Required empty public constructor
    }

    public static SchoolSettingsFragment newInstance(Bundle bundle) {
        SchoolSettingsFragment schoolSettingsFragment = new SchoolSettingsFragment();
        schoolSettingsFragment.setArguments(bundle);
        return schoolSettingsFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (schoolSettingsBinding.schoolMapView != null) {
            schoolSettingsBinding.schoolMapView.onResume();
        }
    }
//    TODO: move the authentication call back in the activity so that wen it is receive the activity passes the
//    new school object tp the fragment

    @Override
    public void onPause() {
        if (schoolSettingsBinding.schoolMapView != null) {
            schoolSettingsBinding.schoolMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (schoolSettingsBinding.schoolMapView != null) {
            try {
                schoolSettingsBinding.schoolMapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (schoolSettingsBinding.schoolMapView != null) {
            schoolSettingsBinding.schoolMapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (schoolSettingsBinding.schoolMapView != null) {
            schoolSettingsBinding.schoolMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        authentication = new Authentication(this);
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        schoolSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_settings, container, false);
        locationService = new AppLocationService(getActivity());
        certificateAdapter = new CertificateAdapter(getActivity());
        achievementAdapter = new CertificateAdapter(getActivity());
        disableAllEdittextFields();
//        if (getArguments() != null && getArguments().containsKey(BundleConstants.SCHOOL_BUNDLE)) ;
//        school = (School) getArguments().getParcelable(BundleConstants.SCHOOL_BUNDLE);
        if (school != null)
            setUpViewWithData(school);
        schoolSettingsBinding.schoolMapView.onCreate(savedInstanceState);
        schoolSettingsBinding.schoolDetailDescriptionText.setOnFocusChangeListener(this);
        schoolSettingsBinding.schoolDetailMottoText.setOnFocusChangeListener(this);
        schoolSettingsBinding.schoolSettingsEditSchoolMotto.setOnClickListener(this);
        schoolSettingsBinding.schoolSettingsEditSchoolBiography.setOnClickListener(this);
        schoolSettingsBinding.schoolSettingsEditSchoolAchievement.setOnClickListener(this);
        schoolSettingsBinding.schoolSettingsEditSchoolCertificates.setOnClickListener(this);
//        schoolSettingsBinding.schoolSettingsEditSchoolChangeLogo.setOnClickListener(this);
        schoolSettingsBinding.schoolSettingsEditSchoolLocation.setOnClickListener(this);
//        schoolSettingsBinding.schoolSettingsEditSchoolChangeLogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "Owner settings image picker button clicked oh");
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//            }
//        });
        schoolSettingsBinding.schoolSettingsEditSchoolCertificates
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "Add cert image picker button clicked oh");
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO_CERT);
                    }
                });
        schoolSettingsBinding.schoolSettingsEditSchoolAchievement
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "Add achievement image picker button clicked oh");
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO_ACHIEVE);
                    }
                });

        return schoolSettingsBinding.getRoot();
    }

    private void setUpViewWithData(School school) {
        if (school == null) return;
        setUpMap();
        schoolSettingsBinding.schoolDetailDescriptionText.setText(school.getSchoolBiography());
        schoolSettingsBinding.schoolDetailMottoText.setText(school.getSchoolMotto());

        if (school.getSchoolLogoImageUrl() != null)
            new PicassoImageLoader(getActivity(), school.getSchoolLogoImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, schoolSettingsBinding.mottoImageview);
//            Picasso.get()
//                    .load(school.getSchoolLogoImageUrl())
//                    .into(schoolSettingsBinding.schoolSettingsLogoImgview);
        certificateAdapter.setCertificateList(school.getCertificates() != null ?
                school.getCertificates() : new ArrayList<Certificate>());
        schoolSettingsBinding.schoolCertificatesRecyclerView
                .setAdapter(certificateAdapter);
        schoolSettingsBinding.schoolCertificatesRecyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        achievementAdapter.setCertificateList(school.getAchievements() != null ?
                school.getAchievements() : new ArrayList<Certificate>());
        schoolSettingsBinding.schoolAchievementRecyclerView
                .setAdapter(achievementAdapter);
        schoolSettingsBinding.schoolAchievementRecyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setUpMap() {
        if (locationService != null) {
//            final Location location = locationService
//                    .getLocation(LocationManager.GPS_PROVIDER);
            if (true) {

                schoolSettingsBinding.schoolMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Log.e(TAG, "map ready  ooooooooh");
                        // Add a marker in Sydney and move the camera
                        LatLng sydney = null;
                        if (school != null)
                            sydney = new LatLng(school.getLatitude(), school.getLongitude());
                        else sydney = new LatLng(-34, 151);
                        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                        googleMap.setLocationSource(new LocationSource() {
//                            @Override
//                            public void activate(OnLocationChangedListener onLocationChangedListener) {
//                                onLocationChangedListener.onLocationChanged(new Location(
//                                        locationService.getLocation(LocationManager.GPS_PROVIDER)
//                                ));
//                                Log.e(TAG, "set location ooooooooh");
//                            }
//
//                            @Override
//                            public void deactivate() {
//
//                            }
//                        });
                    }
                });
                schoolSettingsBinding.schoolMapView.onResume();

            } else {
//                showSettingsAlert();
            }
        }
    }

    /**
     * Action to take to either disable or enable the field when the field edit button
     * is clickec
     *
     * @param editText the edittext to enable or disable
     */
    private void editFieldButtonClicked(EditText editText) {
        editText.setEnabled(!editText.isEnabled());
    }

    private void changeEditButtonIcon(ImageView imageView) {

    }

    /**
     * This method disables all edit text fields
     * when the page is entered
     */
    private void disableAllEdittextFields() {
        schoolSettingsBinding.schoolDetailMottoText.setEnabled(false);
        schoolSettingsBinding.schoolDetailDescriptionText.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.school_settings_edit_school_biography:
//                view.requestFocus();
//                editFieldButtonClicked(schoolSettingsBinding.schoolDetailDescriptionText);
                isBiographyEditMode = !isBiographyEditMode;
                if (!isBiographyEditMode) {
//                    schoolSettingsBinding.schoolSettingsEditSchoolBiography
//                            .setImageResource(R.drawable.ic_edit_white);
                    schoolSettingsBinding.schoolDetailDescriptionText.clearFocus();
                } else {
//                    schoolSettingsBinding.schoolSettingsEditSchoolBiography
//                            .setImageResource(R.drawable.ic_check_white_24dp);
                    schoolSettingsBinding.schoolDetailDescriptionText.requestFocus();
                }
                break;
            case R.id.school_settings_edit_school_motto:
//                editFieldButtonClicked(schoolSettingsBinding.schoolDetailMottoText);
                isMottoEditMode = !isMottoEditMode;
                if (!isMottoEditMode) {
//                    schoolSettingsBinding.schoolSettingsEditSchoolMotto
//                            .setImageResource(R.drawable.ic_edit_white);
                    schoolSettingsBinding.schoolDetailMottoText.clearFocus();
                } else {
//                    schoolSettingsBinding.schoolSettingsEditSchoolMotto
//                            .setImageResource(R.drawable.ic_check_white_24dp);
                    schoolSettingsBinding.schoolDetailMottoText.requestFocus();
                }
                break;
//            case R.id.school_settings_edit_school_change_logo:
//
//                break;
            case R.id.school_settings_edit_school_achievement:
                view.requestFocus();
                break;
            case R.id.school_settings_edit_school_certificates:
                view.requestFocus();
                isCertificateEditMode = !isCertificateEditMode;
//                if(!isCertificateEditMode) schoolSettingsBinding.schoolSettingsEditSchoolCertificates
//                        .setImageResource(R.drawable.ic_edit_white);
//                else  schoolSettingsBinding.schoolSettingsEditSchoolCertificates
//                        .setImageResource(R.drawable.ic_check_white_24dp);
                break;
            case R.id.school_settings_edit_school_location:
                view.requestFocus();
                break;
        }
    }

    private void updateUser(School school) {
        authentication.putNewUserInDb(school);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        //TODO: When it loses focus, query database to make the changes oooh
        boolean textChanged = false;
        switch (view.getId()) {
            case R.id.school_detail_motto_text:
                //If focus change set disable and make the changes
                if (!b) {
                    schoolSettingsBinding.schoolSettingsEditSchoolMotto
                            .setImageResource(R.drawable.ic_edit_white);
                    schoolSettingsBinding.schoolDetailMottoText.setEnabled(false);
                    School school = this.school;
                    if (!schoolSettingsBinding.schoolDetailMottoText.getText().toString().isEmpty()) {
                        if (school.getSchoolMotto() == null) textChanged = true;
                        else if (!school.getSchoolMotto().equals(
                                schoolSettingsBinding.schoolDetailMottoText.getText().toString()))
                            textChanged = true;
                        if (textChanged) {
                            school.setSchoolMotto(schoolSettingsBinding.schoolDetailMottoText.getText().toString());
                            Log.e(TAG, "Schools new motto " + school.getSchoolMotto());
                            updateUser(school);
                        }
                    }
                } else {
                    schoolSettingsBinding.schoolSettingsEditSchoolMotto
                            .setImageResource(R.drawable.ic_check_white);
                    schoolSettingsBinding.schoolDetailMottoText.setEnabled(true);
                }
                break;
            case R.id.school_detail_description_text:
                if (!b) {
                    schoolSettingsBinding.schoolSettingsEditSchoolBiography
                            .setImageResource(R.drawable.ic_edit_white);
                    schoolSettingsBinding.schoolDetailDescriptionText.setEnabled(false);
                    School school = this.school;
                    if (!schoolSettingsBinding.schoolDetailDescriptionText.getText().toString().isEmpty()) {
                        if (school.getSchoolBiography() == null) textChanged = true;
                        else if (!school.getSchoolBiography().equals(
                                schoolSettingsBinding.schoolDetailDescriptionText.getText().toString()))
                            textChanged = true;

                        if (textChanged) {
                            school.setSchoolBiography(schoolSettingsBinding.schoolDetailDescriptionText.getText().toString());
                            Log.e(TAG, "Schools new biography " + school.getSchoolBiography());
                            updateUser(school);
                        }
                    }
                } else {
                    schoolSettingsBinding.schoolSettingsEditSchoolBiography
                            .setImageResource(R.drawable.ic_check_white);
                    schoolSettingsBinding.schoolDetailDescriptionText.setEnabled(true);
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

    }

    @Override
    public void userInsertedToDatabase(School school) {
        Log.e(TAG, "userInsertedToDatabase()");
        if (school != null) {
            this.school = school;
            setUpViewWithData(school);
            if (getActivity() != null)
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

    }

    @Override
    public void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert) {
        if (isCert) {
            List<Certificate> certificates;
            if (school.getCertificates() != null) certificates = school.getCertificates();
            else certificates = new ArrayList<>();

            if (imageUrl != null) {
                if (certDialogFragment != null) {
                    certDialogFragment.dismiss();

                    certificate.setImageOfCert(new Image("", imageUrl, null));
                    certificates.add(certificate);
                    school.setCertificates(certificates);
                    authentication.putNewUserInDb(school);

                }
            } else
                Toast.makeText(getActivity(), "Error saving image, try again!", Toast.LENGTH_SHORT).show();
        } else {
            List<Certificate> achievements;
            if (school.getAchievements() != null) achievements = school.getCertificates();
            else achievements = new ArrayList<>();

            if (imageUrl != null) {
                if (certDialogFragment != null) {
                    certDialogFragment.dismiss();

                    certificate.setImageOfCert(new Image("", imageUrl, null));
                    achievements.add(certificate);
                    school.setAchievements(achievements);
                    authentication.putNewUserInDb(school);

                }
            } else
                Toast.makeText(getActivity(), "Error saving image, try again!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void profileImageDeleted(boolean isSuccessful) {

    }

    @Override
    public void logoStored(String imageUrl) {
//        if (imageUrl != null) {
//            if (imagePreview != null) {
//                imagePreview.dismiss();
//                school.setSchoolLogoImageUrl(imageUrl);
//                authentication.putNewUserInDb(school);
//                Picasso.get()
//                        .load(imageUrl)
//                        .into(schoolSettingsBinding.schoolSettingsLogoImgview);
//            }
//        } else
//            Toast.makeText(getActivity(), "Error saving image, try again!", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (data == null) return;
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                    if (imageUri != null) {
                        imagePreview = DialogFragmentImagePreview
                                .newInstance(this, imageUri, BundleConstants.ACTION_STORE_LOGO);
                        imagePreview.show(getActivity().getSupportFragmentManager(),
                                null);
                    } else
                        Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();

                }
                break;
            case SELECT_PHOTO_CERT:
                if (data == null) return;
                final Uri imageUri2 = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                if (imageUri2 != null) {
                    certDialogFragment = AddCertDialogFragment
                            .newInstance(this, imageUri2, 0, true);
                    certDialogFragment.show(getActivity().getSupportFragmentManager(),
                            null);
                } else
                    Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();
                break;

            case SELECT_PHOTO_ACHIEVE:
                final Uri imageUri3 = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
                if (imageUri3 != null) {
                    certDialogFragment = AddCertDialogFragment
                            .newInstance(this, imageUri3, 0, true);
                    certDialogFragment.show(getActivity().getSupportFragmentManager(), null);
                } else
                    Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
