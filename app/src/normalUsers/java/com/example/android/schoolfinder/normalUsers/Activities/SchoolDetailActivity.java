package com.example.android.schoolfinder.normalUsers.Activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ActivitySchoolDetailBinding;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.normalUsers.Adapters.SchoolDetailPagerAdapter;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchoolDetailActivity extends AppCompatActivity implements FirebaseTransactionCallback {

    private static final String TAG = SchoolDetailActivity.class.getSimpleName();
    private ActivitySchoolDetailBinding schoolDetailBinding;
    private SearchSchoolViewModels viewModels;
    private FirebaseTransactionsAction transactionsAction = new FirebaseTransactionsAction(this);
    private School school;
    List<Image> schoolImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schoolDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_school_detail);
        viewModels = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(SearchSchoolViewModels.class);
        setUpOnCLickListeners();
        setSupportActionBar(schoolDetailBinding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

//        setColorPrimary();

        viewModels.getSchoolLiveData(getSchoolId())
                .observe(this, new Observer<School>() {
                    @Override
                    public void onChanged(@Nullable School schol) {
                        School fomerSchoolObject = school;
                        school = schol;
                        Image image = null;
                        if (school != null && school.getSchoolImages() != null) {
                            schoolImages = school.getSchoolImages();
                            image = schoolImages.get(0);
                            loadImage(image.getImageUrl());
                            setSliderViews();
//                            if(fomerSchoolObject != null) {
//                                if (fomerSchoolObject.getSchoolImages().size() != school.getSchoolImages().size()) {
//                                    //That means there was a change in the images
//                                    setSliderViews();
//                                }
//                            }
                        }
                    }
                });

        schoolDetailBinding.schoolDetailsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                schoolDetailBinding.schoolDetailsViewpager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        // TODO
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        schoolDetailBinding.schoolDetailsTabs.setupWithViewPager(schoolDetailBinding.schoolDetailsViewpager);
        schoolDetailBinding.schoolDetailsViewpager.setAdapter(
                new SchoolDetailPagerAdapter(viewModels, getSupportFragmentManager(), this, getIntent().getExtras()));
    }

    private void setUpOnCLickListeners() {
        schoolDetailBinding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolFollowersAction(schoolDetailBinding.followCount,
                            schoolDetailBinding.followIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        schoolDetailBinding.satisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolImpressedAction(schoolDetailBinding.satisfiedCount,
                            schoolDetailBinding.satisfiedIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        schoolDetailBinding.neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolNormalImpressedAction(schoolDetailBinding.neutralCount,
                            schoolDetailBinding.neutralIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        schoolDetailBinding.dissatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolNotImpressedAction(schoolDetailBinding.dissatisfiedCount,
                            schoolDetailBinding.dissatisfiedIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });
    }

    /**
     * This method sets up the slider images that is displayed in the collapsible toolbar
     */
    private void setSliderViews() {
        if (school.getSchoolImages() == null) return;
        if (school.getSchoolImages().isEmpty()) return;
        setUpViewsWithData();
        for (Image images : schoolImages) {

            SliderView sliderView = new DefaultSliderView(this);
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setImageUrl(images.getImageUrl());
//            sliderView.setDescription(images.getImageTag() != null ? images.getImageTag() : "");

            //at last add this view in your layout :
            schoolDetailBinding.imageSlider.addSliderView(sliderView);
        }
    }

    /**
     * This method is called to set up the views with the data
     */
    private void setUpViewsWithData() {
        schoolDetailBinding.satisfiedCount.setText(String.valueOf(school.getImpressedExpressionCount()));
        schoolDetailBinding.followCount.setText(String.valueOf(school.getFollowersCount()));
        schoolDetailBinding.dissatisfiedCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
        schoolDetailBinding.neutralCount.setText(String.valueOf(school.getNormalExpressionCount()));
        schoolDetailBinding.schoolAddress.setText(school.getSchoolLocation());
        schoolDetailBinding.schoolName.setText(school.getSchoolName());
        hideOrShowIndicator(school.getFollowers(), schoolDetailBinding.followIndicator);
        hideOrShowIndicator(school.getImpressedExpressions(), schoolDetailBinding.satisfiedIndicator);
        hideOrShowIndicator(school.getNormalExpressions(), schoolDetailBinding.neutralIndicator);
        hideOrShowIndicator(school.getNotImpressedExpressions(), schoolDetailBinding.dissatisfiedIndicator);
        if (school.getSchoolLogoImageUrl() != null && !school.getSchoolLogoImageUrl().isEmpty())
            new PicassoImageLoader(this, school.getSchoolLogoImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, schoolDetailBinding.schoolLogoImgview);
    }

    /**
     * This method is to check if the users id id in the map of either follow, satisfied, neutral or
     * dissatisfied. if it is it sets the indicator that the user follows etc else it hides the indicator
     *
     * @param map       map
     * @param indicator a view that indicates
     */
    private void hideOrShowIndicator(Map<String, Boolean> map, View indicator) {
        if (map == null) return;
        if (map.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            indicator.setVisibility(View.VISIBLE);
        else indicator.setVisibility(View.GONE);
    }

    private void setColorPrimary() {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.skool_image1);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {

                    int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                    schoolDetailBinding.collapsingToolbar.setContentScrimColor(vibrantColor);
                    schoolDetailBinding.collapsingToolbar.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            Log.e(TAG, "onCreate: failed to create bitmap from background", e.fillInStackTrace());
            schoolDetailBinding.collapsingToolbar.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
            );
            schoolDetailBinding.collapsingToolbar.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)
            );
        }
    }

    private void loadImage(String url) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imageDimensionHeight =
                (int) getResources().getDimension(R.dimen.school_details_appbar_height);
        int imageDimensionWidth = size.x;

        Picasso
                .get()
                .load(url)
                .resize(imageDimensionWidth, imageDimensionHeight)
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE, NetworkPolicy.NO_CACHE)
                .error(R.drawable.skool_image1)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        schoolDetailBinding.appbarImage.setImageBitmap(bitmap);
                        try {
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {

                                    @SuppressLint("ResourceAsColor") int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                                    @SuppressLint("ResourceAsColor") int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                                    schoolDetailBinding.collapsingToolbar.setContentScrimColor(vibrantColor);
                                    schoolDetailBinding.collapsingToolbar.setStatusBarScrimColor(vibrantDarkColor);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "picasso failed --- " + e.getMessage());
                            // if Bitmap fetch fails, fallback to primary colors
                            Log.e(TAG, "onCreate: failed to create bitmap from background", e.fillInStackTrace());
                            schoolDetailBinding.collapsingToolbar.setContentScrimColor(
                                    ContextCompat.getColor(SchoolDetailActivity.this, R.color.colorPrimary)
                            );
                            schoolDetailBinding.collapsingToolbar.setStatusBarScrimColor(
                                    ContextCompat.getColor(SchoolDetailActivity.this, R.color.colorPrimaryDark)
                            );
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    private School getSchool() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(BundleConstants.SCHOOL_BUNDLE)) {
                return bundle.getParcelable(BundleConstants.SCHOOL_BUNDLE);
            }
        }
        return null;
    }

    private String getSchoolId() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(BundleConstants.SCHOOL_ID_BUNDLE)) {
                return bundle.getString(BundleConstants.SCHOOL_ID_BUNDLE);
            }
        }
        return null;
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
