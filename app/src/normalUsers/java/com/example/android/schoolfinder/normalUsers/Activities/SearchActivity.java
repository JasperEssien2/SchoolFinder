package com.example.android.schoolfinder.normalUsers.Activities;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.example.android.countryregioncitypicker.CountryPickerDialogFragment;
import com.example.android.countryregioncitypicker.Models.Country;
import com.example.android.countryregioncitypicker.OnCountrySelected;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivitySearchBinding;
import com.example.android.schoolfinder.normalUsers.Adapters.SearchStackedCardAdapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.List;

//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Activity} subclass.
 */
public class SearchActivity extends AppCompatActivity implements CardStackListener, OnCountrySelected {

    private static final String TAG = SearchActivity.class.getSimpleName();
    ActivitySearchBinding searchBinding;

    public SearchActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        SearchStackedCardAdapter cardAdapter = new SearchStackedCardAdapter(this, getDummySchoolList());
        setSupportActionBar(searchBinding.toolbar);
//        getSupportActionBar()
//                .setDefaultDisplayHomeAsUpEnabled(true);

        CardStackLayoutManager manager = new CardStackLayoutManager(this, this);
//        manager.setStackFrom(StackFrom.Top);
        manager.setMaxDegree(20f);
        manager.setSwipeThreshold(0.3f);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        CardStackView cardStackView = searchBinding.swipeStack;
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(cardAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        SubMenu subMenu = item.getSubMenu().
        SubMenu subMenu = item.getSubMenu();
//        if(item.getItemId() == R.id.filter_by_category) {
//            Log.e(TAG, "Filter by Category selected -- ");
//
//        }
        switch (item.getItemId()) {
            case R.id.filter_by_country:
                showCountryDialog();
                return true;
            case R.id.filter_by_category:
                subMenu = item.getSubMenu();

                return true;
            case R.id.category_tetiary:
                item.setChecked(!item.isChecked());
                Log.e(TAG, "Tertiary menu -- " + item.isChecked());
                break;

            case R.id.category_high_school:
                item.setChecked(!item.isChecked());
                Log.e(TAG, "High school menu -- " + item.isChecked());
                break;
            case R.id.category_mid_school:
                item.setChecked(!item.isChecked());
                Log.e(TAG, "MidSchool menu -- " + item.isChecked());
                break;
            case R.id.category_kindergarten_nursery:
                item.setChecked(!item.isChecked());
                Log.e(TAG, "Kindergarten nursery menu -- " + item.isChecked());
                break;
            case R.id.category_others:
                item.setChecked(!item.isChecked());
                Log.e(TAG, "Others menu -- " + item.isChecked());
                break;
            case R.id.done:
//                item.getSubMenu().
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    private void initializeView(School school) {

    }

    private void showCountryDialog() {
        CountryPickerDialogFragment dialogFragment = new CountryPickerDialogFragment();
        dialogFragment.initCountrySelectedListener(this);
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private void showStateRegionDialog(int geoNameId) {

    }

    List<Image> getImages(String url) {
        List<Image> images = new ArrayList<>();
        images.add(new Image(null, url, null));
        return images;
    }

    List<School> getDummySchoolList() {
        ArrayList<School> list = new ArrayList<>();
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("http://www.stpetersschools.org/images/inner/St-Peter's-School-Building.jpg"), null, null));
        list.add(new School("", "Ask your father", "Oyigbo, Canada",
                "", "Asking fathers since 1990", "", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("http://media.oregonlive.com/portland_impact/photo/portland-french-school-2e61d93176c0b95c.jpg"), null, null));
        list.add(new School("", "Stupid Hope", "11 Ndia Street",
                "", "Securing stupidness", "", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("https://alsoc.in/wp-content/themes/alsoc/images/2.jpg"), null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("http://media.oregonlive.com/portland_impact/photo/portland-french-school-2e61d93176c0b95c.jpg"), null, null));
        list.add(new School("", "Bright Hope", "11 Ndia Street",
                "", "Securing knowledge", "", "", "", "", "",
                "", null, 10, 2, 200,
                2000, 0, 0, null, null,
                getImages("https://www.wbps.org/cms/lib/NJ01911727/Centricity/ModuleInstance/98/large/school%20front.jpg"), null, null));
        return list;
    }

    @Override
    public void countrySelected(Country country) {
        showStateRegionDialog(country.getGeoNameId());
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
