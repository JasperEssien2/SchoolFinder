package com.example.android.schoolfinder.normalUsers.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ItemSchoolCardBinding;
import com.example.android.schoolfinder.normalUsers.Activities.SchoolDetailActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchStackedCardAdapter extends CardStackView.Adapter<SearchStackedCardAdapter.SearchStackedCardViewHolder> {

    private static final String TAG = SearchStackedCardAdapter.class.getSimpleName();
    private Activity mActivity;
    private List<School> mSchoolList;
    private ItemSchoolCardBinding mItemSchoolCardBinding;

    public SearchStackedCardAdapter(Activity activity, List<School> schoolList) {
        super();
        mActivity = activity;
        mSchoolList = schoolList;
    }

    public SearchStackedCardAdapter() {
        super();
    }

    @NonNull
    @Override
    public SearchStackedCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        mItemSchoolCardBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_school_card, viewGroup, false);

        return new SearchStackedCardViewHolder(mItemSchoolCardBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SearchStackedCardViewHolder viewHolder, int i) {
        School school = mSchoolList.get(viewHolder.getAdapterPosition());
        if (school.getSchoolImages() != null && school.getSchoolImages().get(0) != null) {
            Log.e(TAG, "Image url --- " + school.getSchoolImages().get(0).getImageUrl());
            Picasso
                    .get()
                    .load(school.getSchoolImages().get(0).getImageUrl())
                    .error(R.drawable.skool_image1)
                    .into(viewHolder.backgroundImage);
        }
        viewHolder.schoolName.setText(school.getSchoolName() != null ? school.getSchoolName() : "");
        viewHolder.schoolMotto.setText(school.getSchoolMotto() != null ? school.getSchoolMotto() : "");
        viewHolder.schoolLocation.setText(school.getSchoolLocation() != null ? school.getSchoolLocation() : "");
        viewHolder.schoolNotImpressedExpressionCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
        viewHolder.schoolNormalExpressionCount.setText(String.valueOf(school.getNormalExpressionCount()));
        viewHolder.schoolInterestedCount.setText(String.valueOf(school.getImpressedExpressionCount()));
        viewHolder.schoolFollowersCount.setText(String.valueOf(school.getFollowersCount()));
    }

    @Override
    public int getItemCount() {
        return mSchoolList != null ? mSchoolList.size() : 0;
    }

    public class SearchStackedCardViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView backgroundImage;
        TextView schoolName, schoolLocation, schoolMotto, schoolFbLink, schoolEmailLink, schoolWebsiteLink,
                shoolEmailLink, schoolTwitterLink, schoolFollowersCount, schoolInterestedCount,
                schoolNormalExpressionCount, schoolNotImpressedExpressionCount;
        CircleImageView schoolOwnerImage;

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
            schoolFollowersCount = mItemSchoolCardBinding.followersCount;
            schoolInterestedCount = mItemSchoolCardBinding.smileCountTextview;
            schoolNormalExpressionCount = mItemSchoolCardBinding.neutralCountTextview;
            schoolNotImpressedExpressionCount = mItemSchoolCardBinding.sadCountTextview;
            schoolOwnerImage = mItemSchoolCardBinding.schoolOwnerDetailImageView;
        }

    }
}
