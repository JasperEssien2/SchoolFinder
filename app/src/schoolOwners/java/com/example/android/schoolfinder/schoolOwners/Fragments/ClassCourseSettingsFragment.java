package com.example.android.schoolfinder.schoolOwners.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Adapters.ClassesCourseAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.Authentication;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentClassCourseSettingsBinding;
import com.example.android.schoolfinder.interfaces.AuthenticationCallbacks;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.example.android.schoolfinder.schoolOwners.DialogFragments.AddClassDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.example.android.schoolfinder.schoolOwners.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassCourseSettingsFragment extends Fragment implements AuthenticationCallbacks,
        MediaStorageCallback {

    private FragmentClassCourseSettingsBinding classCourseSettingsBinding;
    private ClassesCourseAdapter adapter;
    private Authentication authentication;
    private School school;

    public static ClassCourseSettingsFragment newInstance(Bundle bundle) {
        ClassCourseSettingsFragment classCourseSettingsFragment = new ClassCourseSettingsFragment();
        classCourseSettingsFragment.setArguments(bundle);
        return classCourseSettingsFragment;
    }

    public ClassCourseSettingsFragment() {
        // Required empty public constructor
    }

    private boolean isClass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication(this);
        authentication.getUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        if (getArguments() != null && getArguments().containsKey(BundleConstants.IS_CLASS_SEETTING)) {
            isClass = getArguments().getBoolean(BundleConstants.IS_CLASS_SEETTING);
        }
    }

    private AddClassDialogFragment dialogFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        classCourseSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_course_settings, container, false);
        if (getArguments() != null && getArguments().containsKey(BundleConstants.IS_CLASS_SEETTING)) {
            isClass = getArguments().getBoolean(BundleConstants.IS_CLASS_SEETTING);
            adapter = new ClassesCourseAdapter(isClass, getActivity());
            setUpRecyclerView();
//            if(isClass)
//                setUpViewsForClass(school);
//            else setUpViewsForCourses(school);
            classCourseSettingsBinding.addCoursesClassesFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isClass) {
                        dialogFragment = new AddClassDialogFragment();
                        if (school != null) {
                            dialogFragment.initCallback(ClassCourseSettingsFragment.this, school);
                            dialogFragment.show(getActivity().getSupportFragmentManager(), null);
                        }

                    }
                }
            });
        }
        return classCourseSettingsBinding.getRoot();
    }

    private void setUpViewsForCourses(School school) {
        if (school.getCourses() != null)
            adapter.setCourseList(school.getCourses());
    }

    private void setUpViewsForClass(School school) {
        if (school.getClasses() != null)
            adapter.setClassList(school.getClasses());

    }

    private void setUpRecyclerView() {
        classCourseSettingsBinding.classCourseSettingsRecyclerview
                .setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
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
        if (isClass) {
            setUpViewsForClass(school);
            if (dialogFragment != null)
                dialogFragment.dismiss();
        } else setUpViewsForCourses(school);
    }

    @Override
    public void loggedOut() {

    }

    @Override
    public void userGotten(School school) {
        this.school = school;
//        setUpViewWithData(school);
        if (isClass) setUpViewsForClass(school);
        else setUpViewsForCourses(school);
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
    public void schoolImageAdded(String imageUrl) {

    }
}
