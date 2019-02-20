package com.example.android.countryregioncitypicker.Models;

import com.google.gson.annotations.SerializedName;

public class StateRegion {
    @SerializedName("toponymName")
    private String toponymName;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("name")
    private String name;
    @SerializedName("countryName")
    private String countryName;
    @SerializedName("countryId")
    private String countryId;
    @SerializedName("adminCode1")
    private String adminCode1;
    @SerializedName("geoNameId")
    private int geoNameId;

    public int getGeoNameId() {
        return geoNameId;
    }

    public void setGeoNameId(int geoNameId) {
        this.geoNameId = geoNameId;
    }

    public String getToponymName() {
        return toponymName;
    }

    public void setToponymName(String toponymName) {
        this.toponymName = toponymName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getAdminCode1() {
        return adminCode1;
    }

    public void setAdminCode1(String adminCode1) {
        this.adminCode1 = adminCode1;
    }
}
