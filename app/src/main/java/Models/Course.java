package Models;

import java.util.List;

public class Course {
    private String id, courseName;
    private List<Users> headTeachersOfCourse;
    private List<Question> courseExamQuestions;
    private boolean isSelectedAsSchoolCourse, isSelectedAsExamCourse;

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
}
