package com.example.android.schoolfinder.normalUsers.Activities;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivitySearchBinding;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.normalUsers.Adapters.SearchStackedCardAdapter;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.google.firebase.auth.FirebaseAuth;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.example.android.schoolfinder.normalUsers.R;

/**
 * A simple {@link Activity} subclass.
 */
public class SearchActivity extends AppCompatActivity implements CardStackListener, OnCountrySelected,
        FirebaseTransactionCallback {

    private static final String TAG = SearchActivity.class.getSimpleName();
    ActivitySearchBinding searchBinding;
    private SearchSchoolViewModels searchSchoolViewModels;
    private FirebaseTransactionsAction transactionsAction;
    private List<School> mSchools;
    private int position;

    public SearchActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        transactionsAction = new FirebaseTransactionsAction(this);
        searchSchoolViewModels = new ViewModelProvider.NewInstanceFactory().create(SearchSchoolViewModels.class);
        setSupportActionBar(searchBinding.toolbar);
        final SearchStackedCardAdapter cardAdapter = new SearchStackedCardAdapter(this, new ArrayList<School>(), transactionsAction);
//        getSupportActionBar()
//                .setDefaultDisplayHomeAsUpEnabled(true);

        CardStackLayoutManager manager = new CardStackLayoutManager(this, this);
//        manager.setStackFrom(StackFrom.Top);
        manager.setMaxDegree(20f);
        manager.setSwipeThreshold(0.3f);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        final CardStackView cardStackView = searchBinding.swipeStack;
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(cardAdapter);
//        cardAdapter.addItems(getDummySchoolList());
        searchSchoolViewModels.getSchoolsLivedata("United States", "California", null)
                .observe(this, new Observer<List<School>>() {

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
                });

        searchBinding.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSchools != null && !mSchools.isEmpty()) {
                    transactionsAction.schoolFollowersAction(searchBinding.followersCount,
                            searchBinding.followButton, mSchools.get(position),
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        searchBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSchools != null && !mSchools.isEmpty()) {
                    transactionsAction.schoolImpressedAction(searchBinding.positiveCount,
                            searchBinding.positiveButton, mSchools.get(position),
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        searchBinding.neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSchools != null && !mSchools.isEmpty()) {
                    transactionsAction.schoolNormalImpressedAction(searchBinding.neutralCount,
                            searchBinding.neutralButton, mSchools.get(position),
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        searchBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSchools != null && !mSchools.isEmpty()) {
                    transactionsAction.schoolNotImpressedAction(searchBinding.negativeCount,
                            searchBinding.negativeButton, mSchools.get(position),
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

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
    public void onCardAppeared(View view, final int position) {
        this.position = position;
        if (mSchools == null) return;
        if (mSchools.isEmpty()) return;
        School school = null;
        try {
            school = mSchools.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
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
