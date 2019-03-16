package com.example.android.schoolfinder.normalUsers.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Adapters.ClassesCourseAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.Course;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentClassCoursesBinding;

import java.util.List;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassCoursesFragment extends Fragment {

    private FragmentClassCoursesBinding binding;
    private ClassesCourseAdapter adapter;
    private School school;

    public ClassCoursesFragment() {
        // Required empty public constructor
    }

    public static ClassCoursesFragment newInstance(Bundle bundle) {
        ClassCoursesFragment fragment = new ClassCoursesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_courses, container, false);
        setUpRecyclerView();
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });
        return binding.getRoot();
    }

    /**
     * This sets up the recycler view to be used in this fragment
     */
    private void setUpRecyclerView() {
        adapter = new ClassesCourseAdapter(false, getActivity(), school);
        adapter.isNormalUser(true);
        binding.coursesRecyclerView
                .setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        binding.coursesRecyclerView
                .setAdapter(adapter);
        if (getCourses() != null) {
            adapter.setCourseList(getCourses());
        }

    }

    /**
     * This method gets the list of courses offered by the class from the bundle passed to this fragment
     *
     * @return a list of courses
     */
    private List<Course> getCourses() {
        if (getArguments() != null) {
            if (getArguments().containsKey(BundleConstants.COURSES_BUNDLE)) {
                return getArguments().getParcelableArrayList(BundleConstants.COURSES_BUNDLE);
            }
        }
        return null;
    }

}
