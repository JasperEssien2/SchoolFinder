package com.example.android.schoolfinder.normalUsers.Fragments;


import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.AppLocationService;
import com.example.android.schoolfinder.databinding.FragmentSchoolDetailBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolDetailFragment extends Fragment {


    private static final String TAG = SchoolDetailFragment.class.getSimpleName();
    private FragmentSchoolDetailBinding detailBinding;
    private AppLocationService locationService;
    private double latitude, longitude;

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
        if (detailBinding.schoolLocationMap != null) {
            detailBinding.schoolLocationMap.onResume();
        }
    }

    @Override
    public void onPause() {
        if (detailBinding.schoolLocationMap != null) {
            detailBinding.schoolLocationMap.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (detailBinding.schoolLocationMap != null) {
            try {
                detailBinding.schoolLocationMap.onDestroy();
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
        if (detailBinding.schoolLocationMap != null) {
            detailBinding.schoolLocationMap.onSaveInstanceState(outState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_detail, container, false);
        locationService = new AppLocationService(getActivity());
        School school = getSchool();
        if (school != null) {
            detailBinding.schoolDetailDescriptionText.setText(school.getSchoolBiography());
            detailBinding.schoolDetailMottoText.setText(school.getSchoolMotto());
            detailBinding.schoolLocationMap.onCreate(savedInstanceState);
            onLocationEdittextClicked();


//        detailBinding.
        }
        return detailBinding.getRoot();
    }

    private void onLocationEdittextClicked() {
        if (locationService != null) {
//            final Location location = locationService
//                    .getLocation(LocationManager.GPS_PROVIDER);
            Log.e(TAG, "onLocationEdittextClicked() called ---  --- ");
//            if (location != null) {

                detailBinding.schoolLocationMap.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Log.e(TAG, "map ready  ooooooooh");
                        // Add a marker in Sydney and move the camera
                        LatLng sydney = new LatLng(-34, 151);
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

}
