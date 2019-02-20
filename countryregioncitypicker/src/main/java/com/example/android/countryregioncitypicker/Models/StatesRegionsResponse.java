package com.example.android.countryregioncitypicker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatesRegionsResponse {

    @SerializedName("totalResultsCount")
    private int totalResultsCount;
    @SerializedName("geonames")
    private List<StateRegion> geonames;

    public int getTotalResultsCount() {
        return totalResultsCount;
    }

    public void setTotalResultsCount(int totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    public List<StateRegion> getGeonames() {
        return geonames;
    }

    public void setGeonames(List<StateRegion> geonames) {
        this.geonames = geonames;
    }
}
