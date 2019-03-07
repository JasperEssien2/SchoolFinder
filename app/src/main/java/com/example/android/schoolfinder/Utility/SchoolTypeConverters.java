package com.example.android.schoolfinder.Utility;

import android.arch.persistence.room.TypeConverter;

import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Class;
import com.example.android.schoolfinder.Models.Course;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SchoolTypeConverters {
    static Gson gson = new Gson();

    @TypeConverter
    public static Users stringToUsers(String data) {
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<Users>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String imageListToUsers(Users users) {
        return gson.toJson(users);
    }

    @TypeConverter
    public static List<Image> stringToImageList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Image>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String imageListToString(List<Image> imageList) {
        return gson.toJson(imageList);
    }

    @TypeConverter
    public static List<Certificate> stringToCertificateList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Certificate>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String certListToString(List<Certificate> certificateList) {
        return gson.toJson(certificateList);
    }

    @TypeConverter
    public static List<Class> stringToClassList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Class>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String classListToString(List<Class> classList) {
        return gson.toJson(classList);
    }

    @TypeConverter
    public static List<Course> stringToCourseList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Course>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String courseListToString(List<Course> courseList) {
        return gson.toJson(courseList);
    }

    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String stringListToString(List<String> stringList) {
        return gson.toJson(stringList);
    }

    @TypeConverter
    public static Map<String, Boolean> stringToMapBoolean(String data) {
        if (data == null) {
            return Collections.EMPTY_MAP;
        }

        Type listType = new TypeToken<Map<String, Boolean>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String booleanMapToString(Map<String, Boolean> booleanMap) {
        return gson.toJson(booleanMap);
    }
}
