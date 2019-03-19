package com.example.android.countryregioncitypicker.Models;

import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("continent")
    private String continent;
    @SerializedName("continentName")
    private String continentName;
    @SerializedName("geonameId")
    private int geoNameId;
    @SerializedName("countryName")
    private String countryName;
    @SerializedName("capital")
    private String capital;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("currencyCode")
    private String currencyCode;

    /**
     * the two fields below are for states
     * when reverse geocoding using lat and long
     */
    @SerializedName("adminName1")
    private String adminName1;
    @SerializedName("adminCode1")
    private String adminCode1;

    public Country(String continent, String continentName, int geoNameId, String countryName, String capital, String countryCode, String currencyCode) {
        this.continent = continent;
        this.continentName = continentName;
        this.geoNameId = geoNameId;
        this.countryName = countryName;
        this.capital = capital;
        this.countryCode = countryCode;
        this.currencyCode = currencyCode;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public int getGeoNameId() {
        return geoNameId;
    }

    public void setGeoNameId(int geoNameId) {
        this.geoNameId = geoNameId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAdminName1() {
        return adminName1;
    }

    public void setAdminName1(String adminName1) {
        this.adminName1 = adminName1;
    }

    public String getAdminCode1() {
        return adminCode1;
    }

    public void setAdminCode1(String adminCode1) {
        this.adminCode1 = adminCode1;
    }

    @Override
    public String toString() {
        return "countryName : " + countryName + "\nCountry Code: " + countryCode + "/geoNameId: " + geoNameId;
    }
}

