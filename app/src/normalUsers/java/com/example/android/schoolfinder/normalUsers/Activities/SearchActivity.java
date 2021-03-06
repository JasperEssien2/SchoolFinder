package com.example.android.schoolfinder.normalUsers.Activities;


import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.example.android.countryregioncitypicker.CountryPickerDialogFragment;
import com.example.android.countryregioncitypicker.Models.Country;
import com.example.android.countryregioncitypicker.Models.StateRegion;
import com.example.android.countryregioncitypicker.OnCountrySelected;
import com.example.android.countryregioncitypicker.OnStateSelected;
import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivitySearchBinding;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.normalUsers.Adapters.SearchStackedCardAdapter;
import com.example.android.schoolfinder.normalUsers.Interfaces.SearchSchoolOfflineDatabaseCallback;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.google.firebase.auth.FirebaseAuth;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Activity} subclass.
 */
public class SearchActivity extends AppCompatActivity implements CardStackListener, OnCountrySelected,
        FirebaseTransactionCallback, SearchSchoolOfflineDatabaseCallback, OnStateSelected {

    private static final String TAG = SearchActivity.class.getSimpleName();
    ActivitySearchBinding searchBinding;
    LiveData<List<School>> schoolListLiveData;
    CountryPickerDialogFragment dialogFragment = new CountryPickerDialogFragment();
    private SearchSchoolViewModels searchSchoolViewModels;
    private FirebaseTransactionsAction transactionsAction = new FirebaseTransactionsAction(this);
    private List<School> mSchools;
    private List<String> categoryList = new ArrayList<>();
    private int position;
    private Observer<List<School>> observer;
    private Country countrySelected;
    private StateRegion stateRegionSelected;
    final SearchStackedCardAdapter cardAdapter = new SearchStackedCardAdapter(this, new ArrayList<School>(),
            transactionsAction, searchSchoolViewModels);
    private String countryCode, stateCode;

    public SearchActivity() {
        // Required empty public constructor
    }

    private boolean isOffline = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        searchSchoolViewModels = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(SearchSchoolViewModels.class);
        cardAdapter.initSchoolViewModels(searchSchoolViewModels);
        searchSchoolViewModels.setOfflineCallback(this);
        setSupportActionBar(searchBinding.toolbar);


//        getSupportActionBar()
//                .setDefaultDisplayHomeAsUpEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        manager.setStackFrom(StackFrom.Top);
//        manager.setMaxDegree(20f);
//        manager.setSwipeThreshold(0.3f);
//        manager.setCanScrollHorizontal(true);
//        manager.setCanScrollVertical(true);
        final RecyclerView cardStackView = searchBinding.swipeStack;
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(cardAdapter);
//        cardAdapter.addItems(getDummySchoolList());

        observer = new Observer<List<School>>() {

            @Override
            public void onChanged(@Nullable List<School> schools) {
                mSchools = schools;
                if (schools != null) {
//                            mSchools.addAll(schools);
                    Log.e(TAG, "School list --- " + schools.toString());
                    cardAdapter.addItems(schools);
//                            cardAdapter.notifyDataSetChanged();
//                            cardAdapter.notifyItemInserted(schools.size() - 1);
                } else
                    Snackbar.make(searchBinding.getRoot(), "No school found", Snackbar.LENGTH_SHORT);
            }
        };
        countryCode = "United States";
        stateCode = "California";
        if (!isOffline)
            schoolListLiveData = searchSchoolViewModels.getSchoolsLivedata(countryCode, stateCode, categoryList);
        else
            schoolListLiveData = searchSchoolViewModels.getOfflineSchoolsLivedata(countryCode, stateCode, categoryList);

        schoolListLiveData.observe(this, observer);

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
            case R.id.saved_school:
                isOffline = !isOffline;
                cardAdapter.isOffline(isOffline);
                item.setTitle(!isOffline ? "Saved Schools" : "online");
                schoolListLiveData.removeObserver(observer);
                if (!isOffline)
                    schoolListLiveData = searchSchoolViewModels.filterByCategories(categoryList);
                else
                    schoolListLiveData = searchSchoolViewModels.getOfflineSchoolsLivedata(countryCode,
                            stateCode, categoryList);
                schoolListLiveData.observe(this, observer);
                Log.e(TAG, "isOffline ------ " + isOffline);

                return true;
            case R.id.category_tetiary:
                item.setChecked(!item.isChecked());
                categoryFilter(0, item);
                Log.e(TAG, "Tertiary menu -- " + item.isChecked());
                break;

            case R.id.category_high_school:
                item.setChecked(!item.isChecked());
                categoryFilter(1, item);
                Log.e(TAG, "High school menu -- " + item.isChecked());
                break;
            case R.id.category_mid_school:
                item.setChecked(!item.isChecked());
                categoryFilter(2, item);
                Log.e(TAG, "MidSchool menu -- " + item.isChecked());
                break;
            case R.id.category_kindergarten_nursery:
                item.setChecked(!item.isChecked());
                categoryFilter(3, item);
                Log.e(TAG, "Kindergarten nursery menu -- " + item.isChecked());
                break;
            case R.id.category_others:
                item.setChecked(!item.isChecked());
                categoryFilter(4, item);
                Log.e(TAG, "Others menu -- " + item.isChecked());
                break;
            case R.id.done:
//                item.getSubMenu().
                return true;
//            case R.id.star_item:
//                item.setChecked(!item.isChecked());
//                starButtonClicked(item);
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method helps update the icon and also saves or remove the item from the database
     *
     * @param item
     */
    private void starButtonClicked(MenuItem item) {
        if (item.isChecked()) {
            item.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            item.setIcon(R.drawable.ic_star_border_black_24dp);
        }
    }

    private void categoryFilter(int itemIndex, MenuItem item) {
        switch (itemIndex) {
            //Tertiary item
            case 0:
                if (item.isChecked()) categoryList.add(FirebaseConstants.TERTIARY_NODE);
                else categoryList.remove(FirebaseConstants.TERTIARY_NODE);
                break;
            //High school item
            case 1:
                if (item.isChecked()) categoryList.add(FirebaseConstants.HIGH_SCHOOLS_NODE);
                else categoryList.remove(FirebaseConstants.HIGH_SCHOOLS_NODE);
                break;
            //Mid school item
            case 2:
                if (item.isChecked()) categoryList.add(FirebaseConstants.PRIMARY_MID_NODE);
                else categoryList.remove(FirebaseConstants.PRIMARY_MID_NODE);
                break;
            //Kindergarten /Nursery item
            case 3:
                if (item.isChecked())
                    categoryList.add(FirebaseConstants.KINDERGARTEN_NURSERIES_NODE);
                else categoryList.remove(FirebaseConstants.KINDERGARTEN_NURSERIES_NODE);
                break;

            //Others item
            case 4:
                if (item.isChecked()) categoryList.add(FirebaseConstants.OTHERS_NODE);
                else categoryList.remove(FirebaseConstants.OTHERS_NODE);
                break;
        }
        schoolListLiveData.removeObserver(observer);
        if (!isOffline)
            schoolListLiveData = searchSchoolViewModels.filterByCategories(categoryList);
        else schoolListLiveData = searchSchoolViewModels.getOfflineSchoolsLivedata(countryCode,
                stateCode, categoryList);
        schoolListLiveData.observe(this, observer);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    private void initializeView(School school) {

    }

    /**
     * This  method is responsible for showing the dialog fragment for selected country to sort by
     */
    private void showCountryDialog() {
        dialogFragment.initCountrySelectedListener(this);
        dialogFragment.isState(false, 0);
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    /**
     * This method is responsible for showing the dialog fragment for selecting stateRegion to sort
     *
     * @param geoNameId the id of the country
     */
    private void showStateRegionDialog(int geoNameId) {
        dialogFragment.initStateRegionSelectedListener(this);
        dialogFragment.isState(true, geoNameId);
        dialogFragment.show(getSupportFragmentManager(), null);
    }


    List<Image> getImages(String url) {
        List<Image> images = new ArrayList<>();
        images.add(new Image(null, url, null));
        return images;
    }

    /**
     * Hides all the fab button when there are no item left
     */
    private void hideAllFabs() {
        searchBinding.positiveFrame.setVisibility(View.GONE);
        searchBinding.negativeFrame.setVisibility(View.GONE);
        searchBinding.neutralFrame.setVisibility(View.GONE);
        searchBinding.followFrame.setVisibility(View.GONE);
    }

    /**
     * Show all the fab button when the list is not empty
     */
    private void showAllFabs() {
        searchBinding.positiveFrame.setVisibility(View.VISIBLE);
        searchBinding.negativeFrame.setVisibility(View.VISIBLE);
        searchBinding.neutralFrame.setVisibility(View.VISIBLE);
        searchBinding.followFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void countrySelected(Country country) {
        countrySelected = country;
        dialogFragment.dismiss();
        Log.e(TAG, "Country selected details ---- " + country.toString());
        showStateRegionDialog(country.getGeoNameId());
    }

    @Override
    public void stateSelected(StateRegion region) {

        stateRegionSelected = region;
        dialogFragment.dismiss();
        if (countrySelected != null && stateRegionSelected != null) {
            schoolListLiveData.removeObserver(observer);
            schoolListLiveData = searchSchoolViewModels.filterByCountryStateRegion(countrySelected.getCountryName(), stateRegionSelected.getName());
            schoolListLiveData.observe(this, observer);
        }
    }

    @Override
    public void schoolAdded() {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void schoolDeleted() {
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void schoolGotten(School school) {

    }

    @Override
    public void schoolsGotten(LiveData<List<School>> listLiveData) {

    }

    @Override
    public void schoolsGotten(List<School> schools) {

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (direction.equals(Direction.Right)) {
            searchSchoolViewModels.insertSchool(mSchools.get(position));
        } else if (direction.equals(Direction.Left)) {
            searchSchoolViewModels.deleteSchool(mSchools.get(position));
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, final int position) {
        this.position = position;
        if (mSchools == null) return;
        if (mSchools.isEmpty()) return;
        showAllFabs();
        School school = null;
        try {
            school = mSchools.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            hideAllFabs();
        }
        if (school != null) {
            searchBinding.negativeCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
            searchBinding.followersCount.setText(String.valueOf(school.getFollowersCount()));
            searchBinding.positiveCount.setText(String.valueOf(school.getImpressedExpressionCount()));
            searchBinding.neutralCount.setText(String.valueOf(school.getNormalExpressionCount()));

            if (school.getNormalExpressions() == null)
                school.setNormalExpressions(new HashMap<String, Boolean>());
            if (school.getNormalExpressions().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                searchBinding.neutralButton.setImageResource(R.drawable.ic_neutral);
            else searchBinding.neutralButton.setImageResource(R.drawable.ic_neutral_deactivated);

            if (school.getNotImpressedExpressions() == null)
                school.setNotImpressedExpressions(new HashMap<String, Boolean>());
            if (school.getNotImpressedExpressions().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                searchBinding.negativeButton.setImageResource(R.drawable.ic_sad__1);
            else searchBinding.negativeButton.setImageResource(R.drawable.ic_sad__1_deactivated);

            if (school.getImpressedExpressions() == null)
                school.setImpressedExpressions(new HashMap<String, Boolean>());
            if (school.getImpressedExpressions().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                searchBinding.positiveButton.setImageResource(R.drawable.ic_smile);
            else searchBinding.positiveButton.setImageResource(R.drawable.ic_smile_deactivated);

//            if(school.getFollowers() == null) school.setFollowers(new HashMap<String, Boolean>());
//            if(school.getFollowers().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//                searchBinding.followButton.setImageResource(R.drawable.ic_neutral);
//            else searchBinding.followButton.setImageResource(R.drawable.ic_neutral_deactivated);
        }
    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    @Override
    public void post(Post post, boolean isSuccessful) {

    }

    @Override
    public void following(School school, boolean isSuccessful) {

    }

    @Override
    public void impressedExpression(School school, boolean isSuccessful) {

    }

    @Override
    public void notImpressedExpression(School school, boolean isSuccessful) {

    }

    @Override
    public void neutralExpression(School school, boolean isSuccessful) {

    }

    @Override
    public void postLike(Post post, boolean isSuccessful) {

    }
}
