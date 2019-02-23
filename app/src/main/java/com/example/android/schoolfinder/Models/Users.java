package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Users implements Parcelable {
    private String id, name, contact, email, location, biography, profileImageUrl, country, state_region, city;
    private double latitude;
    private double longitude;
    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
    private long followingCount = 0;

    public Users() {

    }

    private Map<String, Boolean> following;

    protected Users(Parcel in) {
        id = in.readString();
        name = in.readString();
        contact = in.readString();
        email = in.readString();
        location = in.readString();
        biography = in.readString();
        profileImageUrl = in.readString();
        country = in.readString();
        state_region = in.readString();
        city = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        followingCount = in.readLong();
        following = in.readHashMap(Boolean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(contact);
        dest.writeString(email);
        dest.writeString(location);
        dest.writeString(biography);
        dest.writeString(profileImageUrl);
        dest.writeString(country);
        dest.writeString(state_region);
        dest.writeString(city);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(followingCount);
        dest.writeMap(following);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getState_region() {
        return state_region;
    }

    public void setState_region(String state_region) {
        this.state_region = state_region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }

    public Map<String, Boolean> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, Boolean> following) {
        this.following = following;
    }
}
