package com.example.android.schoolfinder.normalUsers.Fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.FragmentSearchBinding;
import com.example.android.schoolfinder.normalUsers.Adapters.SearchStackedCardAdapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.List;

//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements CardStackListener {

    FragmentSearchBinding searchBinding;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        SearchStackedCardAdapter cardAdapter = new SearchStackedCardAdapter(getActivity(), getDummySchoolList());
        // Inflate the layout for this fragment

        CardStackLayoutManager manager = new CardStackLayoutManager(getActivity(), this);
//        manager.setStackFrom(StackFrom.Top);
        manager.setMaxDegree(20f);
        manager.setSwipeThreshold(0.3f);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        CardStackView cardStackView = searchBinding.swipeStack;
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(cardAdapter);

        return searchBinding.getRoot();
    }

    private void initializeView(School school) {

    }

    List<Image> getImages(String url) {
        List<Image> images = new ArrayList<>();
        images.add(new Image(null, url, null));
        return images;
    }

    List<School> getDummySchoolList(){
        ArrayList<School> list = new ArrayList<>();
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("http://www.stpetersschools.org/images/inner/St-Peter's-School-Building.jpg"), null, null));
        list.add(new School("", "Ask your father", "Oyigbo, Canada",
                "", "Asking fathers since 1990", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("http://media.oregonlive.com/portland_impact/photo/portland-french-school-2e61d93176c0b95c.jpg"), null, null));
        list.add(new School("", "Stupid Hope", "11 Ndia Street",
                "", "Securing stupidness", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("https://alsoc.in/wp-content/themes/alsoc/images/2.jpg"), null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("http://media.oregonlive.com/portland_impact/photo/portland-french-school-2e61d93176c0b95c.jpg"), null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("https://www.wbps.org/cms/lib/NJ01911727/Centricity/ModuleInstance/98/large/school%20front.jpg"), null, null));
        return list;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
