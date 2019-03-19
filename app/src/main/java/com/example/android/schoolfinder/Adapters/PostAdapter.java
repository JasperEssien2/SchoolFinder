package com.example.android.schoolfinder.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.schoolfinder.FirebaseHelper.FirebaseTransactionsAction;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.Utility.PicassoImageLoader;
import com.example.android.schoolfinder.databinding.ItemPostBinding;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final String TAG = PostAdapter.class.getSimpleName();
    private final int HAS_IMAGES = 40, NO_IMAGES = 42;
    private List<Post> postList = new ArrayList<>();
    private Activity activity;
    private boolean isSchool;
    private ItemPostBinding itemPostBinding;
    private FirebaseTransactionsAction transactionsAction;


    public PostAdapter(Activity activity, boolean isSchool) {
        super();
        this.activity = activity;
        this.isSchool = isSchool;
    }

    public void initTransactionsObject(FirebaseTransactionsAction transactionsAction) {

        this.transactionsAction = transactionsAction;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        itemPostBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_post,
                viewGroup, false);

        switch (viewType) {
            case HAS_IMAGES:
                showImagesRecyclerView();
                break;
            case NO_IMAGES:
                hideImagesRecyclerView();
                break;
        }

        return new PostViewHolder(itemPostBinding.getRoot());
    }

    /**
     * This is to set the post list
     *
     * @param postList
     */
    public void setPostList(List<Post> postList) {

        this.postList = postList;
        notifyDataSetChanged();
    }

    /**
     * This is to add an individual post to the list
     *
     * @param post
     */
    public void putPostItem(Post post) {
        if (post == null) return;
        postList.clear();
        postList.add(post);
        Log.e(TAG, "Item inserted -- " + post.toString() + ", index -- " + (postList.size() - 1));
        notifyItemInserted(postList.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int position) {
        int viewType = getItemViewType(postViewHolder.getAdapterPosition());
        Post post = postList.get(postViewHolder.getAdapterPosition());
        if (isSchool)
            bindViewsForSchool(postViewHolder, post, viewType == HAS_IMAGES);
        else bindViewsForNormalUsers(postViewHolder, post, viewType == HAS_IMAGES);


    }

    private void bindViewsForSchool(PostViewHolder postViewHolder, Post post,
                                    boolean hasImages) {
        postViewHolder.starCount.setText(String.valueOf(post.getStarCount()));
        postViewHolder.body.setText(post.getBody());
        if (hasImages)
            setUpImagesRecycler(postViewHolder.images, post);
        if (post.getSchoolLogo() != null && post.getSchoolLogo().getImageUrl() != null)
            new PicassoImageLoader(activity, post.getSchoolLogo().getImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, postViewHolder.logo);
        postViewHolder.time.setReferenceTime((Long) post.getTimeStamp());
    }

    @SuppressLint("RestrictedApi")
    private void bindViewsForNormalUsers(final PostViewHolder postViewHolder, final Post post,
                                         boolean hasImages) {

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}, // checked
                new int[]{android.R.attr.state_pressed},  // pressed
                new int[]{-android.R.attr.state_pressed}  // not pressed
        };

        int[] colors = new int[]{
                Color.GRAY,
                Color.GRAY,
                Color.GRAY,
                R.color.colorCyan200,
                R.color.colorCyan200,
                Color.GRAY
        };

        ColorStateList colorStateList = new ColorStateList(
                states, colors);
        if (hasImages)
            setUpImagesRecycler(postViewHolder.images, post);
        if (post.getSchoolLogo() != null && post.getSchoolLogo().getImageUrl() != null)
            new PicassoImageLoader(activity, post.getSchoolLogo().getImageUrl(), R.color.colorLightGrey,
                    R.color.colorLightGrey, postViewHolder.logo);

        postViewHolder.name.setText(post.getAuthor());
        postViewHolder.starCount.setText(String.valueOf(post.getStarCount()));
        postViewHolder.body.setText(post.getBody());
        postViewHolder.star.setSupportImageTintList(colorStateList);
        postViewHolder.time.setVisibility(View.VISIBLE);
        postViewHolder.time.setReferenceTime((Long) post.getTimeStamp());
        if (post.getStars() == null) post.setStars(new HashMap<String, Boolean>());
        if (post.getStars().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            postViewHolder.star.setPressed(true);
        } else {
            postViewHolder.star.setPressed(false);
        }
        postViewHolder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                postViewHolder.star.setPressed(!postViewHolder.star.isPressed());
                if (transactionsAction != null)
                    transactionsAction.likePost(post, postViewHolder.starCount, postViewHolder.star,
                            FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

    }

    /**
     * This method helps set up the images recyler view
     *
     * @param imagesRecyclerView recyclerview intended
     * @param post               instance of the post so as to get the images
     */
    private void setUpImagesRecycler(RecyclerView imagesRecyclerView, Post post) {
        PostImagesAdapter postImagesAdapter = new PostImagesAdapter(post.getImageList(), activity);
        if (post.getImageList() != null) {
            if (!(post.getImageList().size() > 4))
                //This is so as to make the image item occupy all the width and height of the recycler view
                imagesRecyclerView.setLayoutManager(new GridLayoutManager(activity, post.getImageList().size()));
            else imagesRecyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
            imagesRecyclerView.setAdapter(postImagesAdapter);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Post post = postList.get(position);
        if (post.getImageList() != null && !post.getImageList().isEmpty()) return HAS_IMAGES;
        else return NO_IMAGES;
    }


    /**
     * If there are no images in the post, then this method makes the recycler view for images
     * visibilty gone
     */
    private void hideImagesRecyclerView() {
        itemPostBinding.postImages.setVisibility(View.GONE);
    }

    /**
     * If there are images in the post, then this method makes the recycler view for images
     * visible
     */
    private void showImagesRecyclerView() {
        itemPostBinding.postImages.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    public static class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.PostImagesViewHolder> {

        List<Image> imageList;
        private Activity activity;
        private boolean isGallery;

        public PostImagesAdapter(List<Image> imageList, Activity activity) {
            super();
            this.imageList = imageList;
            this.activity = activity;
        }

        public PostImagesAdapter(List<Image> imageList, Activity activity, boolean isGallery) {
            this.imageList = imageList;
            this.activity = activity;
            this.isGallery = isGallery;
        }

        @NonNull
        @Override
        public PostImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ImageView imageView = new ImageView(activity);
            if (!isGallery) {
                Log.e(TAG, "onCreateViewHolder --- isGallery, " + isGallery);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                imageView.setLayoutParams(new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.MATCH_PARENT));
            } else {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        200));
            }
            return new PostImagesViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull PostImagesViewHolder postImagesViewHolder, int position) {
            Log.e(TAG, "Image size --- " + imageList.size() + ", position -- " + position);
            if (imageList.get(postImagesViewHolder.getAdapterPosition()).getImageUrl() != null &&
                    !imageList.get(postImagesViewHolder.getAdapterPosition()).getImageUrl().isEmpty())
                if (!isGallery)
                    Picasso
                            .get()
                            .load(imageList.get(postImagesViewHolder.getAdapterPosition()).getImageUrl())
                            .placeholder(R.color.colorLightGrey)
                            .into((ImageView) postImagesViewHolder.itemView);
                else Glide
                        .with(activity)
                        .load(imageList.get(postImagesViewHolder.getAdapterPosition()).getImageUrl())
                        .into((ImageView) postImagesViewHolder.itemView);

        }

        public void addImage(Image img) {
            imageList.add(img);
            notifyItemInserted(imageList.size() - 1);
        }

        @Override
        public int getItemCount() {
            return imageList == null ? 0 : imageList.size();
        }

        public void setImages(List<Image> imageList) {
            this.imageList = imageList;
            notifyDataSetChanged();
        }

        private class PostImagesViewHolder extends RecyclerView.ViewHolder {
            public PostImagesViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView logo;
        private RecyclerView images;
        private TextView body, name, starCount;
        private RelativeTimeTextView time;
        private AppCompatImageButton star;
        private ImageView promotedColor;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemPostBinding.postSchoolLogo;
            images = itemPostBinding.postImages;
            body = itemPostBinding.postMesssge;
            name = itemPostBinding.postSchoolName;
            starCount = itemPostBinding.postLikeCount;
            star = itemPostBinding.postLikeButton;
            promotedColor = itemPostBinding.promotedColor;
            time = itemPostBinding.postTime;
        }
    }
}
