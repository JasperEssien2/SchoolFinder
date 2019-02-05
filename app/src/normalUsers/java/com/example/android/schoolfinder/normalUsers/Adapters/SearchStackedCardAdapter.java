package com.example.android.schoolfinder.normalUsers.Adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ItemSchoolCardBinding;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchStackedCardAdapter extends BaseAdapter {

    private List<School> mSchoolList;
    private ItemSchoolCardBinding mItemSchoolCardBinding;

    public SearchStackedCardAdapter(List<School> schoolList) {
        super();
        mSchoolList = schoolList;
    }

    static class ViewHolder {
        RoundedImageView backgroundImage;
        TextView schoolName, schoolLocation, schoolMotto, schoolFbLink, schoolEmailLink, schoolWebsiteLink,
                shoolEmailLink, schoolTwitterLink, schoolFollowersCount, schoolInterestedCount,
                schoolNormalExpressionCount, schoolNotImpressedExpressionCount;
        CircleImageView schoolOwnerImage;

        ViewHolder(ItemSchoolCardBinding schoolCardBinding) {
            backgroundImage = schoolCardBinding.schoolCardBackgroundImage;
            schoolName = schoolCardBinding.itemSchoolName;
            schoolLocation = schoolCardBinding.itemSchoolLocation;
            schoolMotto = schoolCardBinding.itemSchoolMotto;
            schoolFollowersCount = schoolCardBinding.followersCount;
            schoolInterestedCount = schoolCardBinding.smileCountTextview;
            schoolNormalExpressionCount = schoolCardBinding.neutralCountTextview;
            schoolNotImpressedExpressionCount = schoolCardBinding.sadCountTextview;
            schoolOwnerImage = schoolCardBinding.schoolOwnerDetailImageView;
        }
    }

    @Override
    public int getCount() {
        return mSchoolList != null ? mSchoolList.size() : 0;
    }

    @Override
    public School getItem(int i) {
        return mSchoolList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        if (view == null) {
            mItemSchoolCardBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_school_card, viewGroup, false);
            view = mItemSchoolCardBinding.getRoot();
            viewHolder = new ViewHolder(mItemSchoolCardBinding);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            setViewData(viewHolder, mSchoolList.get(i));
        }

        return mItemSchoolCardBinding.getRoot();
    }

    private void setViewData(ViewHolder viewHolder, School school) {
        viewHolder.schoolName.setText(school.getSchoolName() != null ? school.getSchoolName() : "");
        viewHolder.schoolMotto.setText(school.getSchoolMotto() != null ? school.getSchoolMotto() : "");
        viewHolder.schoolLocation.setText(school.getSchoolLocation() != null ? school.getSchoolLocation() : "");
        viewHolder.schoolNotImpressedExpressionCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
        viewHolder.schoolNormalExpressionCount.setText(String.valueOf(school.getNormalExpressionCount()));
        viewHolder.schoolInterestedCount.setText(String.valueOf(school.getImpressedExpressionCount()));
        viewHolder.schoolFollowersCount.setText(String.valueOf(school.getFollowersCount()));
    }
}
