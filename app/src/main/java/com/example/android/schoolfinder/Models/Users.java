package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    private String id, name, contact, email, location, biography, profileImageUrl;
    private long latitude;

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

    public Users() {

    }

    protected Users(Parcel in) {
        id = in.readString();
        name = in.readString();
        contact = in.readString();
        email = in.readString();
        location = in.readString();
        biography = in.readString();
        profileImageUrl = in.readString();
        latitude = in.readLong();
        longitude = in.readLong();
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    private long longitude;

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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(contact);
        parcel.writeString(email);
        parcel.writeString(location);
        parcel.writeString(biography);
        parcel.writeString(profileImageUrl);
        parcel.writeLong(latitude);
        parcel.writeLong(longitude);
    }
}
