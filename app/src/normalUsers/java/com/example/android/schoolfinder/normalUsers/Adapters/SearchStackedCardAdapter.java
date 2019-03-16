package com.example.android.schoolfinder.normalUsers.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ItemSchoolCardBinding;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.normalUsers.Activities.SchoolDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchStackedCardAdapter extends RecyclerView.Adapter<SearchStackedCardAdapter.SearchStackedCardViewHolder>
        implements FirebaseTransactionCallback {

    private static final String TAG = SearchStackedCardAdapter.class.getSimpleName();
    private FirebaseTransactionsAction transactionsAction;
    private Activity mActivity;
    private List<School> mSchoolList;
    //    private final SearchSchoolViewModels viewModels;
    private ItemSchoolCardBinding mItemSchoolCardBinding;

    public SearchStackedCardAdapter(Activity activity, List<School> schoolList,
                                    FirebaseTransactionsAction transactionsAction) {
        super();
        mActivity = activity;
        mSchoolList = schoolList;
        this.transactionsAction = transactionsAction;
//        mTransactionsAction = transactionsAction;
    }

    @NonNull
    @Override
    public SearchStackedCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        mItemSchoolCardBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_school_card, viewGroup, false);
        ViewCompat.setElevation(mItemSchoolCardBinding.schoolLogo, 15.0f);
        return new SearchStackedCardViewHolder(mItemSchoolCardBinding.getRoot());
    }


    @Override
    public void onBindViewHolder(@NonNull final SearchStackedCardViewHolder viewHolder, int i) {
        final School school = mSchoolList.get(viewHolder.getAdapterPosition());
        if (school == null) return;
//        new PicassoImageLoader(mActivity, "https://images.megapixl.com/3337/33377575.jpg", R.color.colorLightGrey,
//                R.color.colorLightGrey, viewHolder.backgroundImage);

        if (school.getSchoolLogoImageUrl() != null && !school.getSchoolLogoImageUrl().isEmpty())
            new PicassoImageLoader(mActivity, school.getSchoolLogoImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, viewHolder.schoolLogo);

        if (school.getSchoolImages() != null && school.getSchoolImages().get(0) != null)
            new PicassoImageLoader(mActivity, school.getSchoolImages().get(0).getImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, viewHolder.backgroundImage);
//        if (school.getSchoolImages() != null && school.getSchoolImages().get(0) != null) {
//            Log.e(TAG, "Image url --- " + school.getSchoolImages().get(0).getImageUrl());
//            new PicassoImageLoader(mActivity, "https://jooinn.com/images/school-building-3.jpg", R.color.colorLightGrey,
//                    R.color.colorLightGrey, viewHolder.backgroundImage);
//
//            new PicassoImageLoader(mActivity, school.getSchoolLogoImageUrl() != null ? school.getSchoolLogoImageUrl() : "", R.color.colorLightGrey,
//                    R.color.colorLightGrey, viewHolder.backgroundImage);
////            Picasso
////                    .get()
////                    .load(school.getSchoolImages().get(0).getImageUrl())
////                    .placeholder(R.color.colorGrey)
////                    .error(R.drawable.skool_image1)
////                    .into(viewHolder.backgroundImage);
//        }
        viewHolder.schoolName.setText(school.getSchoolName() != null ? school.getSchoolName() : "");
        viewHolder.schoolMotto.setText(school.getSchoolMotto() != null ? school.getSchoolMotto() : "");
        viewHolder.schoolLocation.setText(school.getSchoolLocation() != null ? school.getSchoolLocation() : "");
        setUpOnCLickListeners(school);
        viewHolder.satisfiedCount.setText(String.valueOf(school.getImpressedExpressionCount()));
        viewHolder.followCount.setText(String.valueOf(school.getFollowersCount()));
        viewHolder.dissatisfiedCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
        viewHolder.neutralCount.setText(String.valueOf(school.getNormalExpressionCount()));
//        viewHolder.schoolAddress.setText(school.getSchoolLocation());
        viewHolder.schoolName.setText(school.getSchoolName());
        hideOrShowIndicator(school.getFollowers(), viewHolder.followIndicator);
        hideOrShowIndicator(school.getImpressedExpressions(), viewHolder.satisfiedIndicator);
        hideOrShowIndicator(school.getNormalExpressions(), viewHolder.neutralIndicator);
        hideOrShowIndicator(school.getNotImpressedExpressions(), viewHolder.dissatisfiedIndicator);
//        viewHolder.schoolNotImpressedExpressionCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
//        viewHolder.schoolNormalExpressionCount.setText(String.valueOf(school.getNormalExpressionCount()));
//        viewHolder.schoolInterestedCount.setText(String.valueOf(school.getImpressedExpressionCount()));
//        viewHolder.schoolFollowersCount.setText(String.valueOf(school.getFollowersCount()));
//        mItemSchoolCardBinding.followFrame.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mTransactionsAction.schoolFollowersAction(viewHolder.schoolFollowersCount, school,
//                        FirebaseAuth.getInstance().getCurrentUser().getUid());
//            }
//        });
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

    @Override
    public int getItemCount() {
        return mSchoolList != null ? mSchoolList.size() : 0;
    }

    public void addItems(List<School> schools) {
        mSchoolList.clear();
        mSchoolList.addAll(schools);
        this.notifyDataSetChanged();
//        mSchoolList = schools;
//        for(int i = 0; i < schools.size() - 1 ; i++)
//        {
//            mSchoolList.add(schools.get(i));
//            notifyItemInserted(i);
//        }
//        notifyDataSetChanged();
    }

    private void setUpOnCLickListeners(final School school) {
        mItemSchoolCardBinding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolFollowersAction(mItemSchoolCardBinding.followCount,
                            mItemSchoolCardBinding.followIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        mItemSchoolCardBinding.satisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolImpressedAction(mItemSchoolCardBinding.satisfiedCount,
                            mItemSchoolCardBinding.satisfiedIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        mItemSchoolCardBinding.neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolNormalImpressedAction(mItemSchoolCardBinding.neutralCount,
                            mItemSchoolCardBinding.neutralIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        mItemSchoolCardBinding.dissatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolNotImpressedAction(mItemSchoolCardBinding.dissatisfiedCount,
                            mItemSchoolCardBinding.dissatisfiedIndicator, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });
    }

    public class SearchStackedCardViewHolder extends RecyclerView.ViewHolder {
        public TextView satisfiedCount, followCount, neutralCount, dissatisfiedCount;
        public CircleImageView followIndicator, satisfiedIndicator, neutralIndicator, dissatisfiedIndicator;
        RoundedImageView backgroundImage;
        TextView schoolName, schoolLocation, schoolMotto, schoolFbLink, schoolEmailLink, schoolWebsiteLink,
                shoolEmailLink, schoolTwitterLink;
        CircleImageView schoolLogo;

        public SearchStackedCardViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, SchoolDetailActivity.class);
                    intent.putExtra(BundleConstants.SCHOOL_BUNDLE, mSchoolList.get(getAdapterPosition()));
                    intent.putExtra(BundleConstants.SCHOOL_ID_BUNDLE, mSchoolList.get(getAdapterPosition()).getId());
                    mActivity.startActivity(intent);
                }
            });
            backgroundImage = mItemSchoolCardBinding.schoolCardBackgroundImage;
            schoolName = mItemSchoolCardBinding.itemSchoolName;
            schoolLocation = mItemSchoolCardBinding.itemSchoolLocation;
            schoolMotto = mItemSchoolCardBinding.itemSchoolMotto;
            satisfiedCount = mItemSchoolCardBinding.satisfiedCount;
            followCount = mItemSchoolCardBinding.followCount;
            neutralCount = mItemSchoolCardBinding.neutralCount;
            dissatisfiedCount = mItemSchoolCardBinding.dissatisfiedCount;
            followIndicator = mItemSchoolCardBinding.followIndicator;
            satisfiedIndicator = mItemSchoolCardBinding.satisfiedIndicator;
            neutralIndicator = mItemSchoolCardBinding.neutralIndicator;
            dissatisfiedIndicator = mItemSchoolCardBinding.dissatisfiedIndicator;
//            schoolFollowersCount = mItemSchoolCardBinding.followersCount;
//            schoolInterestedCount = mItemSchoolCardBinding.smileCountTextview;
//            schoolNormalExpressionCount = mItemSchoolCardBinding.neutralCountTextview;
//            schoolNotImpressedExpressionCount = mItemSchoolCardBinding.sadCountTextview;
            schoolLogo = mItemSchoolCardBinding.schoolLogo;
        }

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
