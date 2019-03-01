package com.example.android.schoolfinder.normalUsers.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchStackedCardAdapter extends RecyclerView.Adapter<SearchStackedCardAdapter.SearchStackedCardViewHolder>
        implements FirebaseTransactionCallback {

    private static final String TAG = SearchStackedCardAdapter.class.getSimpleName();
    private FirebaseTransactionsAction mTransactionsAction;
    private Activity mActivity;
    private List<School> mSchoolList;
    private ItemSchoolCardBinding mItemSchoolCardBinding;

    public SearchStackedCardAdapter(Activity activity, List<School> schoolList,
                                    FirebaseTransactionsAction transactionsAction) {
        super();
        mActivity = activity;
        mSchoolList = schoolList;
        transactionsAction = new FirebaseTransactionsAction(this);
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
        if(school == null) return;
        new PicassoImageLoader(mActivity, "https://images.megapixl.com/3337/33377575.jpg", R.color.colorLightGrey,
                R.color.colorLightGrey, viewHolder.backgroundImage);

        if (school.getSchoolLogoImageUrl() != null && !school.getSchoolLogoImageUrl().isEmpty())
            new PicassoImageLoader(mActivity, school.getSchoolLogoImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, viewHolder.schoolLogo);
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

    public class SearchStackedCardViewHolder extends CardStackView.ViewHolder {
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
                    mActivity.startActivity(intent);
                }
            });
            backgroundImage = mItemSchoolCardBinding.schoolCardBackgroundImage;
            schoolName = mItemSchoolCardBinding.itemSchoolName;
            schoolLocation = mItemSchoolCardBinding.itemSchoolLocation;
            schoolMotto = mItemSchoolCardBinding.itemSchoolMotto;
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
