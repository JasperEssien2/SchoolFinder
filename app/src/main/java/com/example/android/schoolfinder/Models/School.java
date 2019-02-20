package com.example.android.schoolfinder.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class School implements Parcelable {
    public static final Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };
    private String id;
    private String schoolName;
    private String schoolLocation;
    private String schoolBiography;
    private String schoolMotto;
    private String schoolContact;
    private String schoolLogoImageUrl;
    private String country;
    private String state_region;
    private String city;
    private double latitude;
    private double longitude;
    private String schoolFacebook, schoolTwitter, schoolEmail, schoolWebsite;
    private Users schoolOwnerDetails;
    private List<Certificate> certificates;
    private List<Image> schoolImages;
    private List<Class> classes;
    private List<Course> courses;
    private List<Certificate> achievements;
    private List<String> schoolCategory;
    private long followersCount;
    private long notImpressedExpressionCount;
    private long impressedExpressionCount;
    private long normalExpressionCount;

    public School() {

    }


    public School(String id, String schoolName, String schoolLocation, String schoolBiography,
                  String schoolMotto, String schoolContact, String schoolLogoImageUrl, String schoolFacebook, String schoolTwitter, String schoolEmail,
                  String schoolWebsite, Users schoolOwnerDetails, long followersCount, long notImpressedExpressionCount,
                  long impressedExpressionCount, long normalExpressionCount, double latitude, double longitude,
                  List<Certificate> certificates, List<Certificate> achievements, List<Image> schoolImages,
                  List<Class> classes, List<Course> courses) {

        this.id = id;
        this.schoolName = schoolName;
        this.schoolLocation = schoolLocation;
        this.schoolBiography = schoolBiography;
        this.schoolMotto = schoolMotto;
        this.schoolContact = schoolContact;
        this.schoolLogoImageUrl = schoolLogoImageUrl;
        this.schoolFacebook = schoolFacebook;
        this.schoolTwitter = schoolTwitter;
        this.schoolEmail = schoolEmail;
        this.schoolWebsite = schoolWebsite;
        this.schoolOwnerDetails = schoolOwnerDetails;
        this.followersCount = followersCount;
        this.notImpressedExpressionCount = notImpressedExpressionCount;
        this.impressedExpressionCount = impressedExpressionCount;
        this.normalExpressionCount = normalExpressionCount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.certificates = certificates;
        this.achievements = achievements;
        this.schoolImages = schoolImages;
        this.classes = classes;
        this.courses = courses;
    }

    protected School(Parcel in) {
        id = in.readString();
        schoolName = in.readString();
        schoolLocation = in.readString();
        schoolBiography = in.readString();
        schoolMotto = in.readString();
        schoolContact = in.readString();
        schoolLogoImageUrl = in.readString();
        country = in.readString();
        state_region = in.readString();
        city = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        schoolFacebook = in.readString();
        schoolTwitter = in.readString();
        schoolEmail = in.readString();
        schoolWebsite = in.readString();
        schoolOwnerDetails = in.readParcelable(Users.class.getClassLoader());
        certificates = in.createTypedArrayList(Certificate.CREATOR);
        achievements = in.createTypedArrayList(Certificate.CREATOR);
        schoolImages = in.createTypedArrayList(Image.CREATOR);
        schoolCategory = in.createStringArrayList();
        classes = in.createTypedArrayList(Class.CREATOR);
        courses = in.createTypedArrayList(Course.CREATOR);
        followersCount = in.readLong();
        notImpressedExpressionCount = in.readLong();
        impressedExpressionCount = in.readLong();
        normalExpressionCount = in.readLong();
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getNotImpressedExpressionCount() {
        return notImpressedExpressionCount;
    }

    public void setNotImpressedExpressionCount(long notImpressedExpressionCount) {
        this.notImpressedExpressionCount = notImpressedExpressionCount;
    }

    public long getImpressedExpressionCount() {
        return impressedExpressionCount;
    }

    public void setImpressedExpressionCount(long impressedExpressionCount) {
        this.impressedExpressionCount = impressedExpressionCount;
    }

    public long getNormalExpressionCount() {
        return normalExpressionCount;
    }

    public void setNormalExpressionCount(long normalExpressionCount) {
        this.normalExpressionCount = normalExpressionCount;
    }

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

    public String getSchoolFacebook() {
        return schoolFacebook;
    }

    public void setSchoolFacebook(String schoolFacebook) {
        this.schoolFacebook = schoolFacebook;
    }

    public String getSchoolTwitter() {
        return schoolTwitter;
    }

    public void setSchoolTwitter(String schoolTwitter) {
        this.schoolTwitter = schoolTwitter;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getSchoolWebsite() {
        return schoolWebsite;
    }

    public void setSchoolWebsite(String schoolWebsite) {
        this.schoolWebsite = schoolWebsite;
    }

    public String getSchoolMotto() {
        return schoolMotto;
    }

    public void setSchoolMotto(String schoolMotto) {
        this.schoolMotto = schoolMotto;
    }

    public Users getSchoolOwnerDetails() {
        return schoolOwnerDetails;
    }

    public void setSchoolOwnerDetails(Users schoolOwnerDetails) {
        this.schoolOwnerDetails = schoolOwnerDetails;
    }

    public String getSchoolContact() {
        return schoolContact;
    }

    public void setSchoolContact(String schoolContact) {
        this.schoolContact = schoolContact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(schoolName);
        parcel.writeString(schoolLocation);
        parcel.writeString(schoolBiography);
        parcel.writeString(schoolMotto);
        parcel.writeString(schoolContact);
        parcel.writeString(schoolLogoImageUrl);
        parcel.writeString(country);
        parcel.writeString(state_region);
        parcel.writeString(city);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(schoolFacebook);
        parcel.writeString(schoolTwitter);
        parcel.writeString(schoolEmail);
        parcel.writeString(schoolWebsite);
        parcel.writeParcelable(schoolOwnerDetails, i);
        parcel.writeTypedList(certificates);
        parcel.writeTypedList(achievements);
        parcel.writeTypedList(schoolImages);
        parcel.writeTypedList(classes);
        parcel.writeTypedList(courses);
        parcel.writeStringList(schoolCategory);
        parcel.writeLong(followersCount);
        parcel.writeLong(notImpressedExpressionCount);
        parcel.writeLong(impressedExpressionCount);
        parcel.writeLong(normalExpressionCount);
    }

    public String getSchoolLogoImageUrl() {
        return schoolLogoImageUrl;
    }

    public void setSchoolLogoImageUrl(String schoolLogoImageUrl) {
        this.schoolLogoImageUrl = schoolLogoImageUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState_region() {
        return state_region;
    }

    public void setState_region(String state_region) {
        this.state_region = state_region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getSchoolCategory() {
        return schoolCategory;
    }

    public void setSchoolCategory(List<String> schoolCategory) {
        this.schoolCategory = schoolCategory;
    }
}
