package Models;

import java.util.List;

public class Class {
    private String id, nameOfClass;
    private List<Course> coursesOffered, coursesRequiredForExam;
    private Users headOfClass;

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
