package Models;

import java.util.List;

import Models.Certificate;
import Models.Class;
import Models.Course;
import Models.Image;

public class School {
    private String id, schoolName, schoolLocation, schoolBiography;
    private long latitude, longitude;
    private List<Certificate> certificates, achievements;
    private List<Image> schoolImages;
    private List<Class> classes;
    private List<Course> courses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolLocation() {
        return schoolLocation;
    }

    public void setSchoolLocation(String schoolLocation) {
        this.schoolLocation = schoolLocation;
    }

    public String getSchoolBiography() {
        return schoolBiography;
    }

    public void setSchoolBiography(String schoolBiography) {
        this.schoolBiography = schoolBiography;
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

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<Certificate> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Certificate> achievements) {
        this.achievements = achievements;
    }

    public List<Image> getSchoolImages() {
        return schoolImages;
    }

    public void setSchoolImages(List<Image> schoolImages) {
        this.schoolImages = schoolImages;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
