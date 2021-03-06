package com.example.android.schoolfinder.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.android.schoolfinder.Utility.SchoolTypeConverters;

import java.util.List;
import java.util.Map;

@Entity(tableName = "schools")
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
    //    @TypeConverters(SchoolTypeConverters.class)
//    @PrimaryKey(autoGenerate = true)
    private int dbPrimaryKey;
    @PrimaryKey()
    @NonNull
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
    @TypeConverters(SchoolTypeConverters.class)
    private Users schoolOwnerDetails;
    @TypeConverters(SchoolTypeConverters.class)
    private List<Certificate> certificates;
    @TypeConverters(SchoolTypeConverters.class)
    private List<Image> schoolImages;
    @TypeConverters(SchoolTypeConverters.class)
    private List<Class> classes;
    @TypeConverters(SchoolTypeConverters.class)
    private List<Course> courses;
    @TypeConverters(SchoolTypeConverters.class)
    private List<Certificate> achievements;
    @TypeConverters(SchoolTypeConverters.class)
    private List<String> schoolCategory;
    private long followersCount;
    private long notImpressedExpressionCount;
    private long impressedExpressionCount;
    private long normalExpressionCount;
    private long postsCount;
    @TypeConverters(SchoolTypeConverters.class)
    private Map<String, Boolean> notImpressedExpressions;
    @TypeConverters(SchoolTypeConverters.class)
    private Map<String, Boolean> impressedExpressions;
    @TypeConverters(SchoolTypeConverters.class)
    private Map<String, Boolean> normalExpressions;
    @TypeConverters(SchoolTypeConverters.class)
    private Map<String, Boolean> posts;
    @TypeConverters(SchoolTypeConverters.class)
    private Map<String, Boolean> followers;


    public School() {

    }

    @Ignore
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

    @Ignore
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
        schoolImages = in.createTypedArrayList(Image.CREATOR);
        classes = in.createTypedArrayList(Class.CREATOR);
        courses = in.createTypedArrayList(Course.CREATOR);
        achievements = in.createTypedArrayList(Certificate.CREATOR);
        schoolCategory = in.createStringArrayList();
        followersCount = in.readLong();
        notImpressedExpressionCount = in.readLong();
        impressedExpressionCount = in.readLong();
        normalExpressionCount = in.readLong();
        postsCount = in.readLong();
        followers = in.readHashMap(Boolean.class.getClassLoader());
        notImpressedExpressions = in.readHashMap(Boolean.class.getClassLoader());
        impressedExpressions = in.readHashMap(Boolean.class.getClassLoader());
        normalExpressions = in.readHashMap(Boolean.class.getClassLoader());
        posts = in.readHashMap(Boolean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(schoolName);
        dest.writeString(schoolLocation);
        dest.writeString(schoolBiography);
        dest.writeString(schoolMotto);
        dest.writeString(schoolContact);
        dest.writeString(schoolLogoImageUrl);
        dest.writeString(country);
        dest.writeString(state_region);
        dest.writeString(city);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(schoolFacebook);
        dest.writeString(schoolTwitter);
        dest.writeString(schoolEmail);
        dest.writeString(schoolWebsite);
        dest.writeParcelable(schoolOwnerDetails, flags);
        dest.writeTypedList(certificates);
        dest.writeTypedList(schoolImages);
        dest.writeTypedList(classes);
        dest.writeTypedList(courses);
        dest.writeTypedList(achievements);
        dest.writeStringList(schoolCategory);
        dest.writeLong(followersCount);
        dest.writeLong(notImpressedExpressionCount);
        dest.writeLong(impressedExpressionCount);
        dest.writeLong(normalExpressionCount);
        dest.writeLong(postsCount);
        dest.writeMap(followers);
        dest.writeMap(notImpressedExpressions);
        dest.writeMap(impressedExpressions);
        dest.writeMap(normalExpressions);
        dest.writeMap(posts);
    }

    public Map<String, Boolean> getNotImpressedExpressions() {
        return notImpressedExpressions;
    }

    public void setNotImpressedExpressions(Map<String, Boolean> notImpressedExpressions) {
        this.notImpressedExpressions = notImpressedExpressions;
    }

    public Map<String, Boolean> getImpressedExpressions() {
        return impressedExpressions;
    }

    public void setImpressedExpressions(Map<String, Boolean> impressedExpressions) {
        this.impressedExpressions = impressedExpressions;
    }

    public Map<String, Boolean> getNormalExpressions() {
        return normalExpressions;
    }

    public void setNormalExpressions(Map<String, Boolean> normalExpressions) {
        this.normalExpressions = normalExpressions;
    }

    public Map<String, Boolean> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.followers = followers;
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

    @Override
    public String toString() {
        return "School name: " + schoolName + "\nSchool Country: " + country + "\nState: " + state_region;
    }

    public Map<String, Boolean> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Boolean> posts) {
        this.posts = posts;
    }

    public long getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(long postsCount) {
        this.postsCount = postsCount;
    }

    public int getDbPrimaryKey() {
        return dbPrimaryKey;
    }

    public void setDbPrimaryKey(int dbPrimaryKey) {
        this.dbPrimaryKey = dbPrimaryKey;
    }
}
