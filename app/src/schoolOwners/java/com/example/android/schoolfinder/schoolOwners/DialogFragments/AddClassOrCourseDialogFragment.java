package com.example.android.schoolfinder.schoolOwners.DialogFragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.Class;
import com.example.android.schoolfinder.Models.Course;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.DialogFragmentAddClassesBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.schoolOwners.Fragments.ClassCourseSettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class AddClassOrCourseDialogFragment extends DialogFragment {

    List<String> classNames = new ArrayList<>();
    List<String> courseNames = new ArrayList<>();
    private AuthenticationCallbacks callbacks;
    private School school;
    private DialogFragmentAddClassesBinding binding;
    private Authentication authentication;
    private ClassCourseSettingsFragment classCourseSettingsFragment;
    private boolean isClass = false;

    public static AddClassOrCourseDialogFragment newInstance(Bundle bundle) {
        AddClassOrCourseDialogFragment f = new AddClassOrCourseDialogFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add_classes, container, false);
        if (getArguments() != null && getArguments().containsKey(BundleConstants.IS_CLASS_SEETTING)) {
            isClass = getArguments().getBoolean(BundleConstants.IS_CLASS_SEETTING);
        }
        binding.addClassesDialogProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (classCourseSettingsFragment != null) {
                    Log.e("onCLicke", "fragment not null ----- ");
                    if (isClass)
                        classCourseSettingsFragment.addClasses(getClasses());
                    else classCourseSettingsFragment.addCourses(getCourses());
                    dismiss();
                }
//                if(callbacks != null && school != null) {
//                    school.setClasses(getClasses());
//                    authentication = new Authentication(callbacks);
//                    authentication.putNewUserInDb(school);
//                }

            }
        });
        binding.addClassesFragmentCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return binding.getRoot();
    }

    public void initCallback(AuthenticationCallbacks callbacks, School school) {
        this.callbacks = callbacks;
        this.school = school;

        if (isClass) {
            if (school.getClasses() == null) return;
            for (Class c : school.getClasses()) {
                classNames.add(c.getNameOfClass());
            }
        } else if (school.getCourses() == null) return;
        for (Course c : school.getCourses()) courseNames.add(c.getCourseName());
    }

    private List<Class> getClasses() {
        List<Class> list = new ArrayList<>();

        //TODO: check for duplicate classes
        if (school.getClasses() != null)
            list = school.getClasses();

        String text = binding.addCourseEditText.getText().toString();
        if (text != null) {
            for (String s : text.split("\n")) {
                if (!classNames.contains(s)) {
                    Class clas = new Class();
                    clas.setNameOfClass(s);
                    list.add(clas);
                }
            }
        }
        return list;
    }

    private List<Course> getCourses() {
        List<Course> list = new ArrayList<>();

        //TODO: check for duplicate classes
        if (school.getCourses() != null)
            list = school.getCourses();

        String text = binding.addCourseEditText.getText().toString();
        if (text != null) {
            for (String s : text.split("\n")) {
                if (!courseNames.contains(s)) {
                    Course course = new Course();
                    course.setCourseName(s);
                    course.setSelectedAsSchoolCourse(true);
                    list.add(course);
                }
            }
        }
        return list;
    }

    public void initFragment(ClassCourseSettingsFragment classCourseSettingsFragment) {

        this.classCourseSettingsFragment = classCourseSettingsFragment;
    }
}
