package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Adapters.ClassesCourseAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Class;
import com.example.android.schoolfinder.Models.Course;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentClassCourseSettingsBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.schoolOwners.DialogFragments.AddClassOrCourseDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassCourseSettingsFragment extends Fragment implements AuthenticationCallbacks,
        MediaStorageCallback {

    private static final String TAG = ClassCourseSettingsFragment.class.getSimpleName();
    private FragmentClassCourseSettingsBinding classCourseSettingsBinding;
    private ClassesCourseAdapter adapter;
    private Authentication authentication;
    private School school;
    private boolean isClass;
    private AddClassOrCourseDialogFragment dialogFragment;

    public ClassCourseSettingsFragment() {
        // Required empty public constructor
    }

    public static ClassCourseSettingsFragment newInstance(Bundle bundle) {
        ClassCourseSettingsFragment classCourseSettingsFragment = new ClassCourseSettingsFragment();
        classCourseSettingsFragment.setArguments(bundle);
        return classCourseSettingsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication(this);
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        if (getArguments() != null && getArguments().containsKey(BundleConstants.IS_CLASS_SEETTING)) {
            isClass = getArguments().getBoolean(BundleConstants.IS_CLASS_SEETTING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        classCourseSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_course_settings, container, false);
        if (getArguments() != null && getArguments().containsKey(BundleConstants.IS_CLASS_SEETTING)) {
//            isClass = getArguments().getBoolean(BundleConstants.IS_CLASS_SEETTING);
            adapter = new ClassesCourseAdapter(!isClass, getActivity(), school);
            setUpRecyclerView();
//            if(isClass)
//                setUpViewsForClass(school);
//            else setUpViewsForCourses(school);
//            classCourseSettingsBinding.
        }
        return classCourseSettingsBinding.getRoot();
    }

    /**
     * This sets up the views intended to be used in the courses setting fragment
     *
     * @param school and instance of the school object
     */
    private void setUpViewsForCourses(School school) {
        if (school.getCourses() != null)
            adapter.setCourseList(school.getCourses());
    }

    /**
     * This sets up the views intended to be used in the class setting fragment
     *
     * @param school and instance of the school object
     */
    private void setUpViewsForClass(School school) {
        if (school.getClasses() != null)
            adapter.setClassList(school.getClasses());

    }

    /**
     * This sets up the recycler view to be used in this fragment
     */
    private void setUpRecyclerView() {
        if (isClass) {
            classCourseSettingsBinding.classCourseSettingsRecyclerview
                    .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        } else {
            classCourseSettingsBinding.classCourseSettingsRecyclerview
                    .setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        }
        classCourseSettingsBinding.classCourseSettingsRecyclerview
                .setAdapter(adapter);

    }

    @Override
    public void login(boolean loggedInSuccessful, FirebaseUser user) {

    }

    @Override
    public void signUp(boolean signedUpSuccessful, FirebaseUser user) {

    }

    @Override
    public void userInsertedToDatabase(Users users) {

    }

    @Override
    public void userInsertedToDatabase(School school) {
        if (school != null) {

            this.school = school;
            if (adapter != null) adapter.setSchool(school);
            if (isClass)
                setUpViewsForClass(school);
            else setUpViewsForCourses(school);
            if (dialogFragment != null)
                dialogFragment.dismiss();
        }
    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {
//        setUpViewWithData(school);
        if (school != null) {
            this.school = school;
            if (adapter != null) adapter.setSchool(school);
            Log.e(TAG, "isClass --- " + isClass);
            if (isClass) setUpViewsForClass(school);
            else setUpViewsForCourses(school);
        }
    }

    @Override
    public void userGotten(Users users) {

    }

    @Override
    public void profileImageStored(String imageUrl, boolean isSuccesful) {

    }

    @Override
    public void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert) {

    }

    @Override
    public void profileImageDeleted(boolean isSuccessful) {

    }

    @Override
    public void logoStored(String imageUrl) {

    }

    @Override
    public void schoolImageAdded(String imageUrl, String tag) {

    }

    @Override
    public void postImageAdded(Post post, boolean isSuccessful) {

    }

    /**
     * This is called by the add class dialog fragment to add classes into the school node
     *
     * @param classes the classes added
     */
    public void addClasses(List<Class> classes) {
        if (classes != null && !classes.isEmpty()) {
            Log.e(TAG, "Classes oooh --- " + classes.toString());
            school.setClasses(classes);
            authentication.putNewUserInDb(school);
        }
    }

    /**
     * This is called by the add class dialog fragment to add courses into the school node
     *
     * @param courses the classes added
     */
    public void addCourses(List<Course> courses) {
        if (courses != null && !courses.isEmpty()) {
            Log.e(TAG, "Courses oooh --- " + courses.toString());
            school.setCourses(courses);
            authentication.putNewUserInDb(school);
        }
    }
}
