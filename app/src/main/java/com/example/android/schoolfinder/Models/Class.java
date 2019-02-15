package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Class implements Parcelable {
    private String id, nameOfClass;
    private List<Course> coursesOffered, coursesRequiredForExam;
    private Users headOfClass;

    public static final Creator<Class> CREATOR = new Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };

    public Class(){

    }

    protected Class(Parcel in) {
        id = in.readString();
        nameOfClass = in.readString();
        coursesOffered = in.createTypedArrayList(Course.CREATOR);
        coursesRequiredForExam = in.createTypedArrayList(Course.CREATOR);
        headOfClass = in.readParcelable(Users.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nameOfClass);
        dest.writeTypedList(coursesOffered);
        dest.writeTypedList(coursesRequiredForExam);
        dest.writeParcelable(headOfClass, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameOfClass() {
        return nameOfClass;
    }

    public void setNameOfClass(String nameOfClass) {
        this.nameOfClass = nameOfClass;
    }

    public List<Course> getCoursesOffered() {
        return coursesOffered;
    }

    public void setCoursesOffered(List<Course> coursesOffered) {
        this.coursesOffered = coursesOffered;
    }

    public List<Course> getCoursesRequiredForExam() {
        return coursesRequiredForExam;
    }

    public void setCoursesRequiredForExam(List<Course> coursesRequiredForExam) {
        this.coursesRequiredForExam = coursesRequiredForExam;
    }

    public Users getHeadOfClass() {
        return headOfClass;
    }

    public void setHeadOfClass(Users headOfClass) {
        this.headOfClass = headOfClass;
    }
}
