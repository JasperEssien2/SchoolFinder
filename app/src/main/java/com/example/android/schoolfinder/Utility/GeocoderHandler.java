package com.example.android.schoolfinder.Utility;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.android.schoolfinder.interfaces.GetLocationCallback;

public class GeocoderHandler extends Handler {

    private GetLocationCallback callback;

    public GeocoderHandler(GetLocationCallback callback) {

        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        String locationAddress;
        switch (msg.what) {
            case 1:
                Bundle bundle = msg.getData();
                locationAddress = bundle.getString("address");
                callback.setAddress(locationAddress, bundle.getString("country"),
                        bundle.getString("state_region"), bundle.getString("city"));
                break;
            default:
                locationAddress = null;
                callback.setAddress(locationAddress, null, null, null);
        }
    }
}