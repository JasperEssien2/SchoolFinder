package com.example.android.schoolfinder.schoolOwners.utilities;

import com.example.android.schoolfinder.Models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is to load default courses for the users to select
 */
public class SetDefaultCourses {

    private final boolean isTertiarySchool;
    private final boolean isHighSchool;
    private final boolean isMidSchool;
    private final boolean isKindergartenSchool;
    private final boolean isOther;
    private String schoolId;

    public SetDefaultCourses(String schoolId, boolean isTertiarySchool, boolean isHighSchool, boolean isMidSchool,
                             boolean isKindergartenSchool, boolean isOther) {

        this.schoolId = schoolId;
        this.isTertiarySchool = isTertiarySchool;
        this.isHighSchool = isHighSchool;
        this.isMidSchool = isMidSchool;
        this.isKindergartenSchool = isKindergartenSchool;
        this.isOther = isOther;
    }

    public void addDefaultCoursesToDatabase() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
//                .child(FirebaseConstants.SCHOOLS_USERS_NODE)
//                .child(schoolId)
//                .child(FirebaseConstants.COURSES_LIST)

    }

    private List<Course> getDefaultCoursesToAdd() {
        List<Course> courses = new ArrayList<>();
        for (Course c : getDefaultCourses()) {
            if (isTertiarySchool)
                if (c.isTertiaryCourse()) courses.add(c);

            if (isHighSchool)
                if (c.isHighSchoolCourse()) courses.add(c);

            if (isMidSchool)
                if (c.isMidSchoolCourse()) courses.add(c);

            if (isKindergartenSchool)
                if (c.isKindergartenCourse()) courses.add(c);

            if (isOther)
                courses.add(c);

        }
        return courses;
    }

    private List<Course> getDefaultCourses() {
        List<Course> courses = new ArrayList<>();

        courses.add(new Course("Physics", false, true, true, false, false));
        courses.add(new Course("Chemistry", false, true, true, false, false));
        courses.add(new Course("Mathematics", false, true, true, true, true));
        courses.add(new Course("Biology", false, true, true, false, false));
        courses.add(new Course("Geography", false, true, true, true, false));
        return courses;
    }
}
