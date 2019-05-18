package com.example.android.schoolfinder.normalUsers.Adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ItemSchoolCard2Binding;
import com.example.android.schoolfinder.databinding.ItemStateRegionCircleBinding;
import com.example.android.schoolfinder.interfaces.FirebaseTransactionCallback;
import com.example.android.schoolfinder.normalUsers.Activities.SchoolDetailActivity;
import com.example.android.schoolfinder.normalUsers.SearchSchoolViewModels;
import com.example.android.schoolfinder.normalUsers.models.StateRegionImage;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchStackedCardAdapter extends RecyclerView.Adapter<SearchStackedCardAdapter.SearchStackedCardViewHolder>
        implements FirebaseTransactionCallback {

    private static final String TAG = SearchStackedCardAdapter.class.getSimpleName();
    private FirebaseTransactionsAction transactionsAction;
    private SearchSchoolViewModels searchSchoolViewModels;
    private Activity mActivity;
    private List<School> mSchoolList;
    private ItemSchoolCard2Binding mItemSchoolCardBinding;

    private boolean multiSelect = false;
    private ArrayList<School> selectedItems = new ArrayList<>();
    private boolean isOffline;
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            selectedItems.clear();
            if (isOffline) menu.add("Delete");
            else menu.add("Save");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (School school : selectedItems) {
                if (!isOffline)
                    searchSchoolViewModels.insertSchool(school);
                else searchSchoolViewModels.deleteSchool(school);
            }
            multiSelect = false;
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            mode.finish();
            notifyDataSetChanged();
        }
    };
//    private boolean isOpen = false;


    public SearchStackedCardAdapter(Activity activity, List<School> schoolList,
                                    FirebaseTransactionsAction transactionsAction, SearchSchoolViewModels searchSchoolViewModels) {
        super();
        mActivity = activity;
        mSchoolList = schoolList;
        this.transactionsAction = transactionsAction;
//        mTransactionsAction = transactionsAction;
        this.searchSchoolViewModels = searchSchoolViewModels;
    }

    @NonNull
    @Override
    public SearchStackedCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        mItemSchoolCardBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_school_card2, viewGroup, false);
        ViewCompat.setElevation(mItemSchoolCardBinding.schoolLogo, 15.0f);
        return new SearchStackedCardViewHolder(mItemSchoolCardBinding.getRoot());
    }


    private ConstraintLayout.LayoutParams layoutParams;

    /**
     * This method is to check if the users id id in the map of either follow, satisfied, neutral or
     * dissatisfied. if it is it sets the indicator that the user follows etc else it hides the indicator
     *
     * @param map       map
     * @param indicator a view that indicates
     */
    private void hideOrShowIndicator(Map<String, Boolean> map, View indicator) {
        if (map == null) {
            indicator.setVisibility(View.GONE);
            return;
        }
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

    @Override
    public void onBindViewHolder(@NonNull final SearchStackedCardViewHolder viewHolder, int i) {
        final School school = mSchoolList.get(viewHolder.getAdapterPosition());
        if (school == null) return;
//        new PicassoImageLoader(mActivity, "https://images.megapixl.com/3337/33377575.jpg", R.color.colorLightGrey,
//                R.color.colorLightGrey, viewHolder.backgroundImage);

        viewHolder.selectItem(school);
        if (school.getSchoolLogoImageUrl() != null && !school.getSchoolLogoImageUrl().isEmpty())
            new PicassoImageLoader(mActivity, school.getSchoolLogoImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, viewHolder.schoolLogo);

        if (school.getSchoolImages() != null && school.getSchoolImages().get(0) != null)
            new PicassoImageLoader(mActivity, school.getSchoolImages().get(0).getImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, viewHolder.backgroundImage);

        viewHolder.schoolName.setText(school.getSchoolName() != null ? school.getSchoolName() : "");
        viewHolder.schoolMotto.setText(school.getSchoolMotto() != null ? school.getSchoolMotto() : "");
        viewHolder.schoolLocation.setText(school.getSchoolLocation() != null ? school.getSchoolLocation() : "");
        setUpOnCLickListeners(school);
//        viewHolder.satisfiedCount.setText(String.valueOf(school.getImpressedExpressionCount()));
//        viewHolder.followCount.setText(String.valueOf(school.getFollowersCount()));
//        viewHolder.dissatisfiedCount.setText(String.valueOf(school.getNotImpressedExpressionCount()));
//        viewHolder.neutralCount.setText(String.valueOf(school.getNormalExpressionCount()));
//        viewHolder.schoolAddress.setText(school.getSchoolLocation());
        viewHolder.schoolName.setText(school.getSchoolName());
//        hideOrShowIndicator(school.getFollowers(), viewHolder.followIndicator);
//        hideOrShowIndicator(school.getImpressedExpressions(), viewHolder.satisfiedIndicator);
//        hideOrShowIndicator(school.getNormalExpressions(), viewHolder.neutralIndicator);
//        hideOrShowIndicator(school.getNotImpressedExpressions(), viewHolder.dissatisfiedIndicator);
    }

    /**
     * To control the Contexual Action bar to show delete instead of save when in offline mode
     *
     * @param isOffline
     */
    public void isOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    private void animateSizeUpOnClick(View view) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
//        objectAnimator.
    }

    private void setUpOnCLickListeners(final School school) {

        mItemSchoolCardBinding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
//                    transactionsAction.schoolFollowersAction(mItemSchoolCardBinding.followCount,
//                            mItemSchoolCardBinding.followIndicator, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }
        });

        mItemSchoolCardBinding.satisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
//                    transactionsAction.schoolImpressedAction(mItemSchoolCardBinding.satisfiedCount,
//                            mItemSchoolCardBinding.satisfiedIndicator, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
                }
            }
        });

        mItemSchoolCardBinding.neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (school != null) {
//                    transactionsAction.schoolNormalImpressedAction(mItemSchoolCardBinding.neutralCount,
//                            mItemSchoolCardBinding.neutralIndicator, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
                }
            }
        });

        mItemSchoolCardBinding.dissatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (school != null) {
//                    transactionsAction.schoolNotImpressedAction(mItemSchoolCardBinding.dissatisfiedCount,
//                            mItemSchoolCardBinding.dissatisfiedIndicator, school,
//                            FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
//                }
            }
        });


    }

    private void animateMovingLeft(final ImageButton imageButton, final int smiley, final int newRightMargin, final SearchStackedCardViewHolder viewHolder) {
        Log.e(TAG, "animateMovingLeft --- smiley Number -- " + smiley + ", newRightMargin : " + newRightMargin);
//        int smiley1 = smiley;
//        int newRightMargin = 0;
//        if(smiley == 0) newRightMargin = 8;
//        else if(smiley == 1) newRightMargin = 16;
//        else if(smiley == 2) newRightMargin = 24;
        imageButton.setVisibility(View.VISIBLE);

        final Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                layoutParams = (ConstraintLayout.LayoutParams) imageButton.getLayoutParams();
                if (smiley != 0) layoutParams.rightMargin = 8;
                imageButton.setLayoutParams(layoutParams);
//                newRightMargin = convertPxToDp(newRightMargin);
                Log.e(TAG, "newIghtMarging for smiley number " + smiley + ", " + newRightMargin * interpolatedTime);

                layoutParams.rightMargin = (int) (newRightMargin * interpolatedTime);
//                layoutParams.leftMargin = (int) (storeMargins.get("topAndStartMargin").get(1) * interpolatedTime);
//                layoutParams.setMarginStart((int) (storeMargins.get("topAndStartMargin").get(1) * interpolatedTime));
                imageButton.setLayoutParams(layoutParams);
            }
        };

        a.setDuration(700); // in ms
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageButton.setVisibility(View.VISIBLE);
//                a.setStartOffset(2000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                if(smiley == 0) smiley1 = 1;
//                if(smiley == 0) animateMovingLeft(viewHolder.neutral, 1, 24, viewHolder);
//                else if(smiley == 1) animateMovingLeft(viewHolder.satisfied, 2, 32, viewHolder);
//                a.setRepeatMode(Animation.RESTART);
//                a.setRepeatCount(Animation.INFINITE);
//                animation.setStartOffset(2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        imageButton.anim
        imageButton.setAnimation(a);
    }

    private void animateMovingRight(final ImageButton imageButton, final int smiley, final int newRightMargin, final SearchStackedCardViewHolder viewHolder) {
        Log.e(TAG, "animateMovingLeft --- smiley Number -- " + smiley + ", newRightMargin : " + newRightMargin);
//        int smiley1 = smiley;
//        int newRightMargin = 0;
//        if(smiley == 0) newRightMargin = 8;
//        else if(smiley == 1) newRightMargin = 16;
//        else if(smiley == 2) newRightMargin = 24;
//        imageButton.setVisibility(View.INVISIBLE);

        final Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                layoutParams = (ConstraintLayout.LayoutParams) imageButton.getLayoutParams();
                if (smiley != 0) layoutParams.rightMargin = 8;
                imageButton.setLayoutParams(layoutParams);
                Log.e(TAG, "newIghtMarging for smiley number " + smiley + ", " + newRightMargin * interpolatedTime);

                layoutParams.rightMargin = (int) (newRightMargin * interpolatedTime);
//                layoutParams.leftMargin = (int) (storeMargins.get("topAndStartMargin").get(1) * interpolatedTime);
//                layoutParams.setMarginStart((int) (storeMargins.get("topAndStartMargin").get(1) * interpolatedTime));
                imageButton.setLayoutParams(layoutParams);
            }
        };

        a.setDuration(900); // in ms
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                a.setStartOffset(2000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                if(smiley == 0) smiley1 = 1;
                imageButton.setVisibility(View.
                        INVISIBLE);

                if (smiley == 0) animateMovingRight(viewHolder.neutral, 1, 0, viewHolder);
                else if (smiley == 1) animateMovingRight(viewHolder.satisfied, 2, 0, viewHolder);
//                a.setRepeatMode(Animation.RESTART);
//                a.setRepeatCount(Animation.INFINITE);
//                animation.setStartOffset(2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        imageButton.anim
        imageButton.setAnimation(a);
    }

    /**
     * This method gets the dp of px passed
     *
     * @param val an int of dimen px
     * @return an int of type dp
     */
    private int getDp(int val) {
//        float d = getContext().getResources().getDisplayMetrics().density;
//        int margin = (int)(val * d); // margin in pixels
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val,
                mActivity.getResources().getDisplayMetrics());
//        return margin;
    }

    /**
     * this method converts px to dp
     *
     * @param px dimen in px
     * @return an int in dp
     */
    public int convertPxToDp(int px) {
        return (int) (px / mActivity.getResources().getDisplayMetrics().density);
    }


    public void initSchoolViewModels(SearchSchoolViewModels searchSchoolViewModels) {

        this.searchSchoolViewModels = searchSchoolViewModels;
    }

    @Override
    public void postLike(Post post, boolean isSuccessful) {

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

    public static class StateRegionAdapter extends RecyclerView.Adapter<StateRegionAdapter.StateRegionViewHolder> {

        private final Activity activity;
        private List<StateRegionImage> regionImages;
        private ItemStateRegionCircleBinding binding;
        private StateRegionImage selectedCurrently, previouslySelected;

        public StateRegionAdapter(Activity activity, List<StateRegionImage> regionImages) {
            super();
            this.activity = activity;
            this.regionImages = regionImages;
            if (regionImages == null) return;
            try {
                regionImages.get(0).setSelected(true);
                selectedCurrently = regionImages.get(0);

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }

        @NonNull
        @Override
        public StateRegionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_state_region_circle, viewGroup, false);

            return new StateRegionViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull final StateRegionViewHolder stateRegionViewHolder, int i) {
            StateRegionImage stateRegionImage = regionImages.get(stateRegionViewHolder.getAdapterPosition());
            if (stateRegionImage.getStateImageUrl() != null && !stateRegionImage.getStateImageUrl().isEmpty())
                new PicassoImageLoader(activity, stateRegionImage.getStateImageUrl(), R.color.colorLightGrey, R.color.colorLightGrey,
                        stateRegionViewHolder.imageView);
            stateRegionViewHolder.stateName.setText(stateRegionImage.getToponymName());
            if (stateRegionImage.isSelected())
                stateRegionViewHolder.selectedImageView.setVisibility(View.VISIBLE);
            else stateRegionViewHolder.selectedImageView.setVisibility(View.GONE);
            stateRegionViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    previouslySelected = selectedCurrently;
                    selectedCurrently = regionImages.get(stateRegionViewHolder.getAdapterPosition());
                    selectItem(selectedCurrently);
                    unSelectItem(previouslySelected);
                }
            });
        }


        public void selectItem(StateRegionImage image) {
            image.setSelected(true);
            notifyItemChanged(regionImages.indexOf(image));
        }

        public void unSelectItem(StateRegionImage image) {
            image.setSelected(false);
            notifyItemChanged(regionImages.indexOf(image));
        }

        public void setItems(List<StateRegionImage> regionImages) {

            this.regionImages = regionImages;
            if (regionImages == null) return;
            regionImages.get(0).setSelected(true);
            selectedCurrently = regionImages.get(0);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return regionImages == null ? 0 : regionImages.size();
        }

        public class StateRegionViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView imageView;
            private CircleImageView selectedImageView;
            private TextView stateName;

            public StateRegionViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = binding.stateRegionImage;
                stateName = binding.stateRegionName;
                selectedImageView = binding.stateRegionImageSelected;
            }
        }

    }

    public class SearchStackedCardViewHolder extends RecyclerView.ViewHolder {
        public TextView satisfiedCount, followCount, neutralCount, dissatisfiedCount;
        public CircleImageView followIndicator, satisfiedIndicator, neutralIndicator, dissatisfiedIndicator;
        public ImageButton openCloseRating;
        public Button followButton;
        RoundedImageView backgroundImage;
        TextView schoolName, schoolLocation, schoolMotto, schoolFbLink, schoolEmailLink, schoolWebsiteLink,
                shoolEmailLink, schoolTwitterLink;
        ImageButton satisfied, neutral, disatisfied;
        CircleImageView schoolLogo;
        boolean isOpen = false;

        public SearchStackedCardViewHolder(@NonNull View itemView) {
            super(itemView);

            if (getAdapterPosition() > 0)
                update(mSchoolList.get(getAdapterPosition()));
            else update(null);
//            try {
//                selectItem(mSchoolList.get(getAdapterPosition()));
//            }catch (ArrayIndexOutOfBoundsException e){
//                e.printStackTrace();
//            }
            followButton = mItemSchoolCardBinding.follow;
            backgroundImage = mItemSchoolCardBinding.schoolCardBackgroundImage;
            schoolName = mItemSchoolCardBinding.itemSchoolName;
            schoolLocation = mItemSchoolCardBinding.itemSchoolLocation;
            schoolMotto = mItemSchoolCardBinding.itemSchoolMotto;
            satisfied = mItemSchoolCardBinding.satisfied;
            neutral = mItemSchoolCardBinding.neutral;
            disatisfied = mItemSchoolCardBinding.dissatisfied;
            openCloseRating = mItemSchoolCardBinding.openCloseRating;
//            satisfiedCount = mItemSchoolCardBinding.satisfiedCount;
            followCount = mItemSchoolCardBinding.followCount;
//            neutralCount = mItemSchoolCardBinding.neutralCount;
//            dissatisfiedCount = mItemSchoolCardBinding.dissatisfiedCount;
//            followIndicator = mItemSchoolCardBinding.followIndicator;
//            satisfiedIndicator = mItemSchoolCardBinding.satisfiedIndicator;
//            neutralIndicator = mItemSchoolCardBinding.neutralIndicator;
//            dissatisfiedIndicator = mItemSchoolCardBinding.dissatisfiedIndicator;
//            schoolFollowersCount = mItemSchoolCardBinding.followersCount;
//            schoolInterestedCount = mItemSchoolCardBinding.smileCountTextview;
//            schoolNormalExpressionCount = mItemSchoolCardBinding.neutralCountTextview;
//            schoolNotImpressedExpressionCount = mItemSchoolCardBinding.sadCountTextview;
            schoolLogo = mItemSchoolCardBinding.schoolLogo;
            this.openCloseRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isOpen = !isOpen;
                    if (isOpen) {
                        animateMovingLeft(disatisfied, 0, 16, SearchStackedCardViewHolder.this);
                        animateMovingLeft(neutral, 1, 98, SearchStackedCardViewHolder.this);
                        animateMovingLeft(satisfied, 2, 98, SearchStackedCardViewHolder.this);
//                    isOpen = false;
                        followButton.setVisibility(View.GONE);
                    } else {
//                    isOpen = true;
                        animateMovingRight(disatisfied, 0, 0, SearchStackedCardViewHolder.this);
                        followButton.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

        void update(School item) {
            if (item != null) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    itemView.findViewById(R.id.selected_indicator).setVisibility(View.GONE);
                } else {
                    selectedItems.add(item);
                    itemView.findViewById(R.id.selected_indicator).setVisibility(View.VISIBLE);
                }
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity) mActivity).startSupportActionMode(actionModeCallbacks);
                    selectItem(mSchoolList.get(getAdapterPosition()));
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != -1) {
                        selectItem(mSchoolList.get(getAdapterPosition()));
                        if (!multiSelect) {
                            Intent intent = new Intent(mActivity, SchoolDetailActivity.class);
                            intent.putExtra(BundleConstants.SCHOOL_BUNDLE, mSchoolList.get(getAdapterPosition()));
                            intent.putExtra(BundleConstants.SCHOOL_ID_BUNDLE, mSchoolList.get(getAdapterPosition()).getId());
                            mActivity.startActivity(intent);
                        }
                    }
                }
            });


        }

        void selectItem(School item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    itemView.findViewById(R.id.selected_indicator).setVisibility(View.GONE);
                } else {
                    selectedItems.add(item);
                    itemView.findViewById(R.id.selected_indicator).setVisibility(View.VISIBLE);
                }
            } else {
                itemView.findViewById(R.id.selected_indicator).setVisibility(View.GONE);

            }
        }

    }
}
