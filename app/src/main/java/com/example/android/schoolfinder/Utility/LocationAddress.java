package com.example.android.schoolfinder.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.android.schoolfinder.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolSignUpFragment;

public class LocationAddress {
    private static final String TAG = LocationAddress.class.getSimpleName();

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Activity activity, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                String country =null, state_region = null, city = null;

                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    Log.e(TAG, "try clause");
                    Log.e(TAG, "adressList != null && addressList.size() > 0 " + (addressList != null && addressList.size() > 0));
//                    Log.e(TAG, "AddressList " + addressList.toString());
//                    Log.e(TAG, )
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder stringBuilder = new StringBuilder();
//                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                            stringBuilder.append(address.getAddressLine(i)).append("\n");
//                        }
                        stringBuilder.append(address.getLocality()).append("\n");
//                        stringBuilder.append(address.getPostalCode()).append("\n");
                        stringBuilder.append(address.getCountryName()).append("\n");
//                        stringBuilder.append(address.getPhone()).append("\n");
                        result = stringBuilder.toString();
                        country = address.getCountryName();
                        state_region = address.getLocality();
                        city = address.getSubLocality();
                        Log.e(TAG, result + "****************************");
                        Log.e(TAG, "Country -- " + country + "\n " + "StateRegion --" + state_region + "\n CIty " + city);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
                        builder.setTitle("Unable to get address.");
                        builder.setMessage("Please Ok to try again!");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        Looper.prepare();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Log.e(TAG, "final clause");
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        bundle.putString("country", country);
                        bundle.putString("state_region", state_region);
                        bundle.putString("city", city);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", null);
                        bundle.putString("country", null);
                        bundle.putString("state_region", null);
                        bundle.putString("city", null);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    private void getLocation() {

    }
}
