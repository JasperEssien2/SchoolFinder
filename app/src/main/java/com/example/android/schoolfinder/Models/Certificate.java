package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Certificate implements Parcelable {
    private String id, name, description;
    private Image imageOfCert;

    public static final Creator<Certificate> CREATOR = new Creator<Certificate>() {
        @Override
        public Certificate createFromParcel(Parcel in) {
            return new Certificate(in);
        }

        @Override
        public Certificate[] newArray(int size) {
            return new Certificate[size];
        }
    };

    protected Certificate(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImageOfCert() {
        return imageOfCert;
    }

    public void setImageOfCert(Image imageOfCert) {
        this.imageOfCert = imageOfCert;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
    }
}
