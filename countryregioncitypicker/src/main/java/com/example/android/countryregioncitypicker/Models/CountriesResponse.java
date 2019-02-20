package com.example.android.countryregioncitypicker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountriesResponse {

    @SerializedName("geonames")
    private List<Country> geoNames;

    public List<Country> getGeoNames() {
        return geoNames;
    }

    public void setGeoNames(List<Country> geoNames) {
        this.geoNames = geoNames;
    }
}
