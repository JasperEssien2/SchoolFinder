package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Course implements Parcelable {
    private String id;
    private String courseName;
    private Users headTeacherOfCourse;
    private List<Question> courseExamQuestions;
    private boolean isSelectedAsSchoolCourse;
    private boolean isSelectedAsExamCourse;
    /**
     * The fields below control which default course list will be added to the prompt
     */
    private boolean isTertiaryCourse;
    private boolean isHighSchoolCourse;
    private boolean isMidSchoolCourse;
    private boolean isKindergartenCourse;


    public Course(String courseName, boolean isSelectedAsSchoolCourse, boolean isTertiaryCourse,
                  boolean isHighSchoolCourse, boolean isMidSchoolCourse, boolean isKindergartenCourse) {

        this.courseName = courseName;
        this.isSelectedAsSchoolCourse = isSelectedAsSchoolCourse;
        this.isTertiaryCourse = isTertiaryCourse;
        this.isHighSchoolCourse = isHighSchoolCourse;
        this.isMidSchoolCourse = isMidSchoolCourse;
        this.isKindergartenCourse = isKindergartenCourse;
    }

    public Course() {

    }

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

    public boolean isHighSchoolCourse() {
        return isHighSchoolCourse;
    }

    public void setHighSchoolCourse(boolean highSchoolCourse) {
        isHighSchoolCourse = highSchoolCourse;
    }

    public boolean isMidSchoolCourse() {
        return isMidSchoolCourse;
    }

    public void setMidSchoolCourse(boolean midSchoolCourse) {
        isMidSchoolCourse = midSchoolCourse;
    }

    public boolean isKindergartenCourse() {
        return isKindergartenCourse;
    }

    public void setKindergartenCourse(boolean kindergartenCourse) {
        isKindergartenCourse = kindergartenCourse;
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

    public boolean isTertiaryCourse() {
        return isTertiaryCourse;
    }

    public void setTertiaryCourse(boolean tertiaryCourse) {
        isTertiaryCourse = tertiaryCourse;
    }
}
