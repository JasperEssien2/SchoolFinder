package com.example.android.schoolfinder.normalUsers.Fragments;


import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Adapters.CertificateAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.AppLocationService;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.FragmentSchoolDetailBinding;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolDetailFragment extends Fragment {


    private static final String TAG = SchoolDetailFragment.class.getSimpleName();
    private FragmentSchoolDetailBinding detailBinding;
    private AppLocationService locationService;
    private double latitude, longitude;
    private School school;
    private SearchSchoolViewModels viewModels;
    //    private FirebaseTransactionsAction transactionsAction = new FirebaseTransactionsAction(this);
    private CertificateAdapter achievementAdapter, certificateAdapter;


    public SchoolDetailFragment() {
        // Required empty public constructor
    }


    public static SchoolDetailFragment newInstance(Bundle bundle) {
        SchoolDetailFragment schoolDetailFragment = new SchoolDetailFragment();
        schoolDetailFragment.setArguments(bundle);
        return schoolDetailFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (detailBinding != null)
                if (detailBinding.schoolLocationMap != null) {
                    detailBinding.schoolLocationMap.onResume();
                }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        if (detailBinding.schoolLocationMap != null) {
            try {
                detailBinding.schoolLocationMap.onPause();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (detailBinding.schoolLocationMap != null) {
            try {
//                detailBinding.schoolLocationMap.onDestroy();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (detailBinding.schoolLocationMap != null) {
            detailBinding.schoolLocationMap.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            if (detailBinding.schoolLocationMap != null) {
                detailBinding.schoolLocationMap.onSaveInstanceState(outState);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        detailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_detail, container, false);
        locationService = new AppLocationService(getActivity());
        certificateAdapter = new CertificateAdapter(getActivity());
        achievementAdapter = new CertificateAdapter(getActivity());
//        school = getSchool();
        if (viewModels != null)
            viewModels.getSchoolLiveData(getSchoolId())
                    .observe(this, new Observer<School>() {
                        @Override
                        public void onChanged(@Nullable School schol) {
                            school = schol;
                            Log.e(TAG, "onChanged() ------------- ");
                            if (school != null) {
                                setUpViewWithData(savedInstanceState);
                                onLocationEdittextClicked();
                            }
                        }
                    });


        return detailBinding.getRoot();
    }

    private void setUpViewWithData(Bundle savedInstanceState) {
        Log.e(TAG, "setUpViewWithData()   ------ school " + school.toString());
        detailBinding.schoolDetailDescriptionText.setText(school.getSchoolBiography());
        detailBinding.schoolDetailMottoText.setText(school.getSchoolMotto());
        detailBinding.schoolLocationMap.onCreate(savedInstanceState);
//        detailBinding.smileCountTextview.setText(String.valueOf(school.getImpressedExpressionCount()));
//        detailBinding.followersCount.setText(String.valueOf(school.getFollowersCount()));
//        detailBinding.sadCountTextview.setText(String.valueOf(school.getNotImpressedExpressionCount()));
//        detailBinding.neutralCountTextview.setText(String.valueOf(school.getNormalExpressionCount()));
        if (mGoogleMap != null) {
            LatLng sydney = new LatLng(school.getLatitude(), school.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        if (school.getSchoolLogoImageUrl() != null && !school.getSchoolLogoImageUrl().isEmpty())
            new PicassoImageLoader(getActivity(), school.getSchoolLogoImageUrl(), R.color.colorLightGrey, R.color.colorLightGrey, detailBinding.mottoImageView);
        certificateAdapter.setCertificateList(school.getCertificates() != null ?
                school.getCertificates() : new ArrayList<Certificate>());
        detailBinding.schoolCertificatesRecyclerView
                .setAdapter(certificateAdapter);
        detailBinding.schoolCertificatesRecyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        achievementAdapter.setCertificateList(school.getAchievements() != null ?
                school.getAchievements() : new ArrayList<Certificate>());
        detailBinding.schoolAchievementRecyclerView
                .setAdapter(achievementAdapter);
        detailBinding.schoolAchievementRecyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        Users schoolOwner = school.getSchoolOwnerDetails();
        if (schoolOwner != null) {
            detailBinding.schoolDetailOwnerDetailsName.setText(schoolOwner.getName());
            detailBinding.schoolDetailOwnerDetailsBiography.setText(schoolOwner.getBiography());
            if (schoolOwner.getProfileImageUrl() != null && !schoolOwner.getProfileImageUrl().isEmpty()) {
                new PicassoImageLoader(getActivity(), schoolOwner.getProfileImageUrl(), R.color.colorLightGrey,
                        R.color.colorLightGrey, detailBinding.imageViewSchoolOwner);
            }
        }
    }

//    private void setUpOnCLickListeners() {
//        detailBinding.followFrame.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (school != null) {
//                    transactionsAction.schoolFollowersAction(detailBinding.followersCount,
//                            detailBinding.followFrame, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid());
//                }
//            }
//        });
//
//        detailBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (school != null) {
//                    transactionsAction.schoolImpressedAction(detailBinding.smileCountTextview,
//                            detailBinding.positiveButton, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid());
//                }
//            }
//        });
//
//        detailBinding.neutralButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (school != null) {
//                    transactionsAction.schoolNormalImpressedAction(detailBinding.neutralCountTextview,
//                            detailBinding.neutralButton, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid());
//                }
//            }
//        });
//
//        detailBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (school != null) {
//                    transactionsAction.schoolNotImpressedAction(detailBinding.sadCountTextview,
//                            detailBinding.negativeButton, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid());
//                }
//            }
//        });
//    }

    private GoogleMap mGoogleMap;

    private void onLocationEdittextClicked() {
        if (locationService != null) {
//            final Location location = locationService
//                    .getLocation(LocationManager.GPS_PROVIDER);
            Log.e(TAG, "onLocationEdittextClicked() called ---  --- ");
//            if (location != null) {

            detailBinding.schoolLocationMap.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    Log.e(TAG, "map ready  ooooooooh");
                    // Add a marker in Sydney and move the camera
                    LatLng sydney = new LatLng(school.getLatitude(), school.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
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

//            } else {
////                showSettingsAlert();
//            }
        }
    }

    private School getSchool() {
        if (getArguments() != null) {
            if (getArguments().containsKey(BundleConstants.SCHOOL_BUNDLE)) {
                return getArguments().getParcelable(BundleConstants.SCHOOL_BUNDLE);
            }
        }
        return null;
    }

    private String getSchoolId() {
        if (getArguments() != null) {
            if (getArguments().containsKey(BundleConstants.SCHOOL_ID_BUNDLE)) {
                return getArguments().getString(BundleConstants.SCHOOL_ID_BUNDLE);
            }
        }
        return null;
    }

    public void setViewModel(SearchSchoolViewModels viewModels) {

        this.viewModels = viewModels;
    }

}
