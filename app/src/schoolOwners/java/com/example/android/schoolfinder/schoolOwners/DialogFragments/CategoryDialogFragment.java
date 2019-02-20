package com.example.android.schoolfinder.schoolOwners.DialogFragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.DialogFragmentCategoryBinding;
import com.example.android.schoolfinder.schoolOwners.Fragments.SchoolSignUpFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    private DialogFragmentCategoryBinding binding;
    private SchoolSignUpFragment mSchoolSignUpFragment;
    private List<String> mCategories = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_category, container, false);
        binding.checkboxTertiary.setOnCheckedChangeListener(this);
        binding.checkboxHighSchool.setOnCheckedChangeListener(this);
        binding.checkboxMidSchool.setOnCheckedChangeListener(this);
        binding.checkboxKindergartenNursery.setOnCheckedChangeListener(this);
        binding.checkboxOthers.setOnCheckedChangeListener(this);
        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSchoolSignUpFragment.setSchoolCategories(mCategories);
                dismiss();
            }
        });
        return binding.getRoot();
    }

    /**
     * This method is called to initialixe the school signup fragment field
     * @param schoolSignUpFragment
     */
    public void initSchoolSignUpFragment(SchoolSignUpFragment schoolSignUpFragment) {

        mSchoolSignUpFragment = schoolSignUpFragment;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == binding.checkboxTertiary.getId()) {
            if (b) mCategories.add(FirebaseConstants.TERTIARY_NODE);
            else mCategories.remove(FirebaseConstants.TERTIARY_NODE);
        } else if (compoundButton.getId() == binding.checkboxHighSchool.getId()) {
            if (b) mCategories.add(FirebaseConstants.HIGH_SCHOOLS_NODE);
            else mCategories.remove(FirebaseConstants.HIGH_SCHOOLS_NODE);
        } else if (compoundButton.getId() == binding.checkboxMidSchool.getId()) {
            if (b) mCategories.add(FirebaseConstants.PRIMARY_MID_NODE);
            else mCategories.remove(FirebaseConstants.PRIMARY_MID_NODE);
        } else if (compoundButton.getId() == binding.checkboxKindergartenNursery.getId()) {
            if (b) mCategories.add(FirebaseConstants.KINDERGARTEN_NURSERIES_NODE);
            else mCategories.remove(FirebaseConstants.KINDERGARTEN_NURSERIES_NODE);
        } else if (compoundButton.getId() == binding.checkboxOthers.getId()) {
            if (b) mCategories.add(FirebaseConstants.OTHERS_NODE);
            else mCategories.remove(FirebaseConstants.OTHERS_NODE);
        }
    }
}
