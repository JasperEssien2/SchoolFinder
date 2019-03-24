package com.example.android.schoolfinder.normalUsers.DialogFragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.DialogFragmentRatingBinding;

public class RatingDialogFragment extends DialogFragment {
    private DialogFragmentRatingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_rating, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog_background_15dp);
//        getDialog().
        return binding.getRoot();
    }
}
