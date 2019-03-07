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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ActivitySchoolDetailBinding;
import com.example.android.schoolfinder.normalUsers.Adapters.SchoolDetailPagerAdapter;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class SchoolDetailActivity extends AppCompatActivity {

    private static final String TAG = SchoolDetailActivity.class.getSimpleName();
    private ActivitySchoolDetailBinding schoolDetailBinding;
    private SearchSchoolViewModels viewModels;
    private School school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schoolDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_school_detail);
        viewModels = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(SearchSchoolViewModels.class);

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
                            image = school.getSchoolImages().get(0);
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

    private void setSliderViews() {
        if (school.getSchoolImages() == null) return;
        if (school.getSchoolImages().isEmpty()) return;

        for (Image images : school.getSchoolImages()) {

            SliderView sliderView = new DefaultSliderView(this);
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setImageUrl(images.getImageUrl());
//            sliderView.setDescription(images.getImageTag() != null ? images.getImageTag() : "");

            //at last add this view in your layout :
            schoolDetailBinding.imageSlider.addSliderView(sliderView);
        }
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

}
