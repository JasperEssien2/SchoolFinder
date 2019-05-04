package com.example.android.schoolfinder.normalUsers.Activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ActivitySchoolDetail2Binding;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.normalUsers.DialogFragments.RatingDialogFragment;
import com.example.android.schoolfinder.normalUsers.Fragments.DialogFragmentMap;
import com.example.android.schoolfinder.normalUsers.Fragments.SchoolDetailFragment;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class SchoolDetailActivity extends AppCompatActivity implements FirebaseTransactionCallback {

    private static final String TAG = SchoolDetailActivity.class.getSimpleName();
    List<Image> schoolImages = new ArrayList<>();
    private ActivitySchoolDetail2Binding schoolDetailBinding;
    private SearchSchoolViewModels viewModels;
    private FirebaseTransactionsAction transactionsAction = new FirebaseTransactionsAction(this);
    private School school;
    private boolean isFABOpen = false;
    private Animation fab_open;
    private Animation fab_close;
    private Animation fab_icon_rotate_forward;
    private Animation fab_icon_rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schoolDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_school_detail2);
        viewModels = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(SearchSchoolViewModels.class);
        fab_open = AnimationUtils.loadAnimation(this, R.anim.anime_fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.anime_fab_close);
        fab_icon_rotate_forward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_forward);
        fab_icon_rotate_backward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_backward);
        schoolDetailBinding.imageSlider.setPagerIndicatorVisibility(false);
//        setUpOnCLickListeners();
        setSupportActionBar(schoolDetailBinding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schoolDetailBinding.fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });
        schoolDetailBinding.viewSchoolInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school == null) return;
                DialogFragmentMap bottomSheetDialog = DialogFragmentMap.getInstance();
                bottomSheetDialog.initSchool(school);
                bottomSheetDialog.show(getSupportFragmentManager(), "Custom Bottom Sheet");
            }
        });
        schoolDetailBinding.fabRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image image = null;
                if (school == null) return;
                RatingDialogFragment ratingDialogFragment = new RatingDialogFragment();
                if (school.getSchoolImages() != null) {
                    schoolImages = school.getSchoolImages();
                    image = schoolImages.get(0);
                    ratingDialogFragment.setBackgroundImageUrl(image.getImageUrl(),
                            school.getSchoolLogoImageUrl(), school, transactionsAction);
                }
                else ratingDialogFragment.setBackgroundImageUrl(null,
                        school.getSchoolLogoImageUrl(), school, transactionsAction);
                ratingDialogFragment.show(getSupportFragmentManager(), null);
            }
        });

//        setColorPrimary();

        viewModels.getSchoolLiveData(getSchoolId())
                .observe(this, new Observer<School>() {
                    @Override
                    public void onChanged(@Nullable School schol) {
                        School fomerSchoolObject = school;
                        school = schol;
                        Image image = null;
                        setUpViewsWithData();
                        if (school != null && school.getSchoolImages() != null) {
                            schoolImages.clear();
                            schoolImages = school.getSchoolImages();
                            image = schoolImages.get(0);
                            loadImage(image.getImageUrl());
                            Log.e(TAG, "Data Gotten --- School info: --- " + schol.toString());
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

        SchoolDetailFragment schoolDetailFragment = SchoolDetailFragment.newInstance(getIntent().getExtras());
        schoolDetailFragment.setViewModel(viewModels);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(schoolDetailBinding.fragmentDetail.getId(), schoolDetailFragment)
                .commit();
//        schoolDetailBinding.schoolDetailsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                schoolDetailBinding.schoolDetailsViewpager.setCurrentItem(tab.getPosition());
//                switch (tab.getPosition()) {
//                    case 0:
//                        // TODO
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        schoolDetailBinding.schoolDetailsTabs.setupWithViewPager(schoolDetailBinding.schoolDetailsViewpager);
//        schoolDetailBinding.schoolDetailsViewpager.setAdapter(
//                new SchoolDetailPagerAdapter(viewModels, getSupportFragmentManager(), this, getIntent().getExtras()));
    }

    private void animateFab() {
        if (isFABOpen) {
            schoolDetailBinding.fabMore.startAnimation(fab_icon_rotate_backward);

            schoolDetailBinding.fabRating.startAnimation(fab_close);
            schoolDetailBinding.fabSocialMedia.startAnimation(fab_close);
            schoolDetailBinding.commentOnSchoolFab.startAnimation(fab_close);
            schoolDetailBinding.fabRating.setClickable(false);
            schoolDetailBinding.fabSocialMedia.setClickable(false);
            schoolDetailBinding.commentOnSchoolFab.setClickable(false);
            schoolDetailBinding.fabRating.setVisibility(View.INVISIBLE);
            schoolDetailBinding.fabSocialMedia.setVisibility(View.INVISIBLE);
            schoolDetailBinding.commentOnSchoolFab.setVisibility(View.INVISIBLE);
            isFABOpen = false;
        } else {
            schoolDetailBinding.fabMore.startAnimation(fab_icon_rotate_forward);
            schoolDetailBinding.fabRating.startAnimation(fab_open);
            schoolDetailBinding.fabSocialMedia.startAnimation(fab_open);
            schoolDetailBinding.commentOnSchoolFab.startAnimation(fab_open);
            schoolDetailBinding.fabRating.setClickable(true);
            schoolDetailBinding.fabSocialMedia.setClickable(true);
            schoolDetailBinding.commentOnSchoolFab.setClickable(true);
            schoolDetailBinding.fabRating.setVisibility(View.VISIBLE);
            schoolDetailBinding.fabSocialMedia.setVisibility(View.VISIBLE);
            schoolDetailBinding.commentOnSchoolFab.setVisibility(View.VISIBLE);
            isFABOpen = true;
        }
    }


    /**
     * This method sets up the slider images that is displayed in the collapsible toolbar
     */
    private void setSliderViews() {
        if (school.getSchoolImages() == null) return;
        if (school.getSchoolImages().isEmpty()) return;
        schoolDetailBinding.imageSlider.setPagerIndicatorVisibility(false);
//        schoolDetailBinding.imageSlider.

        Log.e(TAG, "setSliderView() --- images count" + schoolImages.size());
        schoolDetailBinding.imageSlider.clearSliderViews();
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
        if (school == null) return;
        schoolDetailBinding.schoolLocation.setText(school.getSchoolLocation());
        schoolDetailBinding.schoolName.setText(school.getSchoolName());

        if (school.getSchoolLogoImageUrl() != null && !school.getSchoolLogoImageUrl().isEmpty())
            new PicassoImageLoader(this, school.getSchoolLogoImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, schoolDetailBinding.schoolLogoImgview);
    }

//    /**
//     * This method is to check if the users id id in the map of either follow, satisfied, neutral or
//     * dissatisfied. if it is it sets the indicator that the user follows etc else it hides the indicator
//     *
//     * @param map       map
//     * @param indicator a view that indicates
//     */
//    private void hideOrShowIndicator(Map<String, Boolean> map, View indicator) {
//        if (map == null) return;
//        if (map.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//            indicator.setVisibility(View.VISIBLE);
//        else indicator.setVisibility(View.GONE);
//    }


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
//                                    schoolDetailBinding.collapsingToolbar.setContentScrimColor(vibrantColor);
//                                    schoolDetailBinding.collapsingToolbar.setStatusBarScrimColor(vibrantDarkColor);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "picasso failed --- " + e.getMessage());
                            // if Bitmap fetch fails, fallback to primary colors
                            Log.e(TAG, "onCreate: failed to create bitmap from background", e.fillInStackTrace());
//                            schoolDetailBinding.collapsingToolbar.setContentScrimColor(
//                                    ContextCompat.getColor(SchoolDetailActivity.this, R.color.colorPrimary)
//                            );
//                            schoolDetailBinding.collapsingToolbar.setStatusBarScrimColor(
//                                    ContextCompat.getColor(SchoolDetailActivity.this, R.color.colorPrimaryDark)
//                            );
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
