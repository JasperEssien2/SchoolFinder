package com.example.android.schoolfinder.normalUsers.DialogFragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.DialogFragmentRating2Binding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingDialogFragment extends DialogFragment {
    private DialogFragmentRating2Binding binding;
    private String url;
    private String logoUrl;
    private School school;
    private FirebaseTransactionsAction transactionsAction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_rating_2, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog_background_15dp);
//        getDialog().
        setUpOnCLickListeners();
        if (url != null) new PicassoImageLoader(getActivity(), url, R.color.colorLightGrey,
                R.color.colorLightGrey, binding.dialogTopSchoolBackground);

        if (logoUrl != null) new PicassoImageLoader(getActivity(), logoUrl, R.color.colorLightGrey,
                R.color.colorLightGrey, binding.schoolLogo);
        binding.positiveCount.setText(String.valueOf(school.getImpressedExpressionCount()));
        binding.followersCount.setText(String.valueOf(school.getFollowersCount()));
        binding.negativeCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
        binding.neutralCount.setText(String.valueOf(school.getNormalExpressionCount()));

        if (school.getFollowers() == null) school.setFollowers(new HashMap<String, Boolean>());
        if (!school.getFollowers().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            binding.followersButton.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorLighterGrey));
        else
            binding.followersButton.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorCyan200));

        viewActiivatedOrNot(school.getImpressedExpressions(), binding.positiveButton, R.drawable.ic_smile, R.drawable.ic_smile_deactivated);
        viewActiivatedOrNot(school.getNormalExpressions(), binding.neutralButton, R.drawable.ic_neutral, R.drawable.ic_neutral_deactivated);
        viewActiivatedOrNot(school.getNotImpressedExpressions(), binding.negativeButton, R.drawable.ic_sad__1, R.drawable.ic_sad__1_deactivated);
        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setBackgroundImageUrl(String url, String logoUrl, School school, FirebaseTransactionsAction transactionsAction) {

        this.url = url;
        this.logoUrl = logoUrl;
        this.school = school;
        this.transactionsAction = transactionsAction;
    }

    private void setUpOnCLickListeners() {
        binding.followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolFollowersAction(binding.followersCount,
                            binding.followersButton, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        binding.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolImpressedAction(binding.positiveCount,
                            binding.positiveButton, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                }
            }
        });

        binding.neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolNormalImpressedAction(binding.neutralCount,
                            binding.neutralButton, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                }
            }
        });

        binding.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
                    transactionsAction.schoolNotImpressedAction(binding.negativeCount,
                            binding.negativeButton, school,
                            FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                }
            }
        });
    }

    /**
     * This method is to check if the users id id in the map of either follow, satisfied, neutral or
     * dissatisfied. if it is it sets the indicator that the user follows etc else it hides the indicator
     *
     * @param map       map
     * @param indicator a view that indicates
     */
    private void viewActiivatedOrNot(Map<String, Boolean> map, CircleImageView indicator, @DrawableRes int resourseActivated,
                                     @DrawableRes int resourseDeactivated) {
        if (map == null) return;
        if (map.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            indicator.setImageResource(resourseActivated);
        else indicator.setImageResource(resourseDeactivated);
    }
}
