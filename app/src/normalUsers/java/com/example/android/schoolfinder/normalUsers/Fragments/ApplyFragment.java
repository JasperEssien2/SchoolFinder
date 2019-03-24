package com.example.android.schoolfinder.normalUsers.Fragments;


import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Adapters.ClassesCourseAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.Course;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentApplyBinding;
import com.example.android.schoolfinder.interfaces.ClassCourseAdapterCallback;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;

import java.util.ArrayList;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplyFragment extends Fragment {

    private static final String TAG = ApplyFragment.class.getSimpleName();
    private FragmentApplyBinding binding;
    private ClassesCourseAdapter adapter;
    private School school;
    private SearchSchoolViewModels viewModels;

    public ApplyFragment() {
        // Required empty public constructor
    }

    public static ApplyFragment newInstance(Bundle bundle) {
        ApplyFragment applyFragment = new ApplyFragment();
        applyFragment.setArguments(bundle);
        return applyFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apply, container, false);
        if (viewModels != null)
            viewModels.getSchoolLiveData(getSchoolId())
                    .observe(this, new Observer<School>() {
                        @Override
                        public void onChanged(@Nullable School schol) {
                            school = schol;
                            if (school != null) {
                                adapter = new ClassesCourseAdapter(false, getActivity(), school);
                                adapter.isNormalUser(true);
                                setUpRecyclerView();
                            }
                        }
                    });

        return binding.getRoot();
    }

    /**
     * This sets up the recycler view to be used in this fragment
     */
    private void setUpRecyclerView() {
        binding.classesRecyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.classesRecyclerView
                .setAdapter(adapter);
        adapter.setClassList(school.getClasses());
        adapter.setCallback(new ClassCourseAdapterCallback() {
            @Override
            public void openCoursesFragment(ArrayList<Course> coursesOffered) {
                openCoursesOfferedFragment(coursesOffered);
            }
        });
    }

    /**
     * This method opens up the courses offered fragment to show courses offered by a class
     *
     * @param courses
     */
    private void openCoursesOfferedFragment(ArrayList<Course> courses) {
        Bundle bundle = new Bundle();
        Log.e(TAG, "openCoursesOfferedFragment() ------------------ ");
        if (school != null) { //TODO: passing in the school courses offered is for test purpose, pass class courses instead
            bundle.putParcelableArrayList(BundleConstants.COURSES_BUNDLE, courses);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.school_detail_parent, ClassCoursesFragment.newInstance(bundle), null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * This method gets the school id from the bundle passed to this fragment
     *
     * @return a string of the school id
     */
    private String getSchoolId() {
        if (getArguments() != null) {
            if (getArguments().containsKey(BundleConstants.SCHOOL_ID_BUNDLE)) {
                return getArguments().getString(BundleConstants.SCHOOL_ID_BUNDLE);
            }
        }
        return null;
    }

    public void setViewModel(SearchSchoolViewModels viewModels) {

        this.viewModels = viewModels;
    }
}
