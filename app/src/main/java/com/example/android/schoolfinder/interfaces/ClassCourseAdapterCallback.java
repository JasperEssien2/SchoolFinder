package com.example.android.schoolfinder.interfaces;

import com.example.android.schoolfinder.Models.Course;

import java.util.ArrayList;

public interface ClassCourseAdapterCallback {
    void openCoursesFragment(ArrayList<Course> coursesOffered);
}
