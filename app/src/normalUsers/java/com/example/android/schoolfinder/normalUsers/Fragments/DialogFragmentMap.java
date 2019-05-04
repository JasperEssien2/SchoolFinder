package com.example.android.schoolfinder.normalUsers.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.AppLocationService;
import com.example.android.schoolfinder.databinding.DialogFragmentMapBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DialogFragmentMap extends BottomSheetDialogFragment {

    private static final String TAG = DialogFragmentMap.class.getSimpleName();
    private DialogFragmentMapBinding binding;
    private AppLocationService locationService;
    private School school;
    private GoogleMap mGoogleMap;

    public static DialogFragmentMap getInstance() {
        return new DialogFragmentMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (binding != null)
                if (binding.schoolLocationMap != null) {
                    binding.schoolLocationMap.onResume();
                }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        if (binding.schoolLocationMap != null) {
            try {
                binding.schoolLocationMap.onPause();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (binding.schoolLocationMap != null) {
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
        if (binding.schoolLocationMap != null) {
            binding.schoolLocationMap.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            if (binding.schoolLocationMap != null) {
                binding.schoolLocationMap.onSaveInstanceState(outState);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void initSchool(School school) {

        this.school = school;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_map, container, false);
        binding.schoolLocationMap.onCreate(savedInstanceState);
        onLocationEdittextClicked();
        if (mGoogleMap != null) {
            locationService = new AppLocationService(getActivity());
            LatLng sydney = new LatLng(school.getLatitude(), school.getLongitude());

            mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
            // Creating CameraUpdate object for position
            CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(sydney);

            // Creating CameraUpdate object for zoom
            CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(4);

            // Updating the camera position to the user input latitude and longitude
            mGoogleMap.moveCamera(updatePosition);

            // Applying zoom to the marker position
            mGoogleMap.animateCamera(updateZoom);
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        return binding.getRoot();
    }

    private void onLocationEdittextClicked() {
        binding.schoolLocationMap.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                Log.e(TAG, "map ready  ooooooooh");
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(school.getLatitude(), school.getLongitude());

                mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
                // Creating CameraUpdate object for position
                CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(sydney);

                // Creating CameraUpdate object for zoom
                CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(4);

                // Updating the camera position to the user input latitude and longitude
                mGoogleMap.moveCamera(updatePosition);

                // Applying zoom to the marker position
                mGoogleMap.animateCamera(updateZoom);
//                        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            }
        });

    }
//    }
//        if (locationService != null) {
//            final Location location = locationService
//                    .getLocation(LocationManager.GPS_PROVIDER);
//            Log.e(TAG, "onLocationEdittextClicked() called ---  --- ");
//            if (location != null) {
//
//
//        }
//    }
}
