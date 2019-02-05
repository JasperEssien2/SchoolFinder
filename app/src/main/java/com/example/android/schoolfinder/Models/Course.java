package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Course implements Parcelable {
    private String id, courseName;
    private List<Users> headTeachersOfCourse;
    private List<Question> courseExamQuestions;
    private boolean isSelectedAsSchoolCourse, isSelectedAsExamCourse;

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    protected Course(Parcel in) {
        id = in.readString();
        courseName = in.readString();
        headTeachersOfCourse = in.createTypedArrayList(Users.CREATOR);
        isSelectedAsSchoolCourse = in.readByte() != 0;
        isSelectedAsExamCourse = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Users> getHeadTeachersOfCourse() {
        return headTeachersOfCourse;
    }

    public void setHeadTeachersOfCourse(List<Users> headTeachersOfCourse) {
        this.headTeachersOfCourse = headTeachersOfCourse;
    }

    public List<Question> getCourseExamQuestions() {
        return courseExamQuestions;
    }

    public void setCourseExamQuestions(List<Question> courseExamQuestions) {
        this.courseExamQuestions = courseExamQuestions;
    }

    public boolean isSelectedAsSchoolCourse() {
        return isSelectedAsSchoolCourse;
    }

    public void setSelectedAsSchoolCourse(boolean selectedAsSchoolCourse) {
        isSelectedAsSchoolCourse = selectedAsSchoolCourse;
    }

    public boolean isSelectedAsExamCourse() {
        return isSelectedAsExamCourse;
    }

    public void setSelectedAsExamCourse(boolean selectedAsExamCourse) {
        isSelectedAsExamCourse = selectedAsExamCourse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(courseName);
        parcel.writeTypedList(headTeachersOfCourse);
        parcel.writeByte((byte) (isSelectedAsSchoolCourse ? 1 : 0));
        parcel.writeByte((byte) (isSelectedAsExamCourse ? 1 : 0));
    }
}
