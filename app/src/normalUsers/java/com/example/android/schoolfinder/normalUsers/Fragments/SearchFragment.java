package com.example.android.schoolfinder.normalUsers.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentSearchBinding;
import com.example.android.schoolfinder.normalUsers.Adapters.SearchStackedCardAdapter;

import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;
//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SwipeStack.SwipeStackListener {

    FragmentSearchBinding searchBinding;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SearchStackedCardAdapter cardAdapter = new SearchStackedCardAdapter(getDummySchoolList());
        // Inflate the layout for this fragment
        searchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        searchBinding.swipeStack.setAdapter(cardAdapter);
        searchBinding.swipeStack.setListener(this);
        searchBinding.swipeStack.setPadding(0, 0, 0, 0);
//        searchBinding.swipeStack.
        return searchBinding.getRoot();
    }

    List<School> getDummySchoolList(){
        ArrayList<School> list = new ArrayList<>();
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null, null, null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null, null, null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null, null, null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null, null, null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null, null, null, null));
        return list;
    }

    @Override
    public void onViewSwipedToLeft(int position) {

    }

    @Override
    public void onViewSwipedToRight(int position) {

    }

    @Override
    public void onStackEmpty() {

    }
}
