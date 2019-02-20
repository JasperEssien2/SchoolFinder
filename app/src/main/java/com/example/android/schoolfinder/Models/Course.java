package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Course implements Parcelable {
    private String id, courseName;
    private Users headTeacherOfCourse;
    private List<Question> courseExamQuestions;
    private boolean isSelectedAsSchoolCourse, isSelectedAsExamCourse;


    protected Course(Parcel in) {
        id = in.readString();
        courseName = in.readString();
        headTeacherOfCourse = in.readParcelable(Users.class.getClassLoader());
        courseExamQuestions = in.createTypedArrayList(Question.CREATOR);
        isSelectedAsSchoolCourse = in.readByte() != 0;
        isSelectedAsExamCourse = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(courseName);
        dest.writeParcelable(headTeacherOfCourse, flags);
        dest.writeTypedList(courseExamQuestions);
        dest.writeByte((byte) (isSelectedAsSchoolCourse ? 1 : 0));
        dest.writeByte((byte) (isSelectedAsExamCourse ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public Users getHeadTeacherOfCourse() {
        return headTeacherOfCourse;
    }

    public void setHeadTeacherOfCourse(Users headTeacherOfCourse) {
        this.headTeacherOfCourse = headTeacherOfCourse;
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

}
