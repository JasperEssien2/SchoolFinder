package com.example.android.schoolfinder.schoolOwners.DialogFragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.android.schoolfinder.Adapters.PostAdapter;
import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.ItemAddPostBinding;
import com.example.android.schoolfinder.schoolOwners.Fragments.ActivitiesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddPostDialogFragment extends DialogFragment {
    private static final int SELECT_PHOTO = 56;
    private static final String TAG = AddPostDialogFragment.class.getSimpleName();
    private ActivitiesFragment mActivitiesFragment;
    private ItemAddPostBinding binding;
    private List<Image> imageList = new ArrayList<>();
    private Image schoolLogoImage;
    private PostAdapter.PostImagesAdapter postImagesAdapter = null;

    public static AddPostDialogFragment newInstance(Bundle bundle) {
        AddPostDialogFragment addPostDialogFragment = new AddPostDialogFragment();
        addPostDialogFragment.setArguments(bundle);
        return addPostDialogFragment;
    }

    /**
     * This is to initialize the activities fragment instance to add post when add post button is clicked
     *
     * @param activitiesFragment
     */
    public void initActivityFragment(ActivitiesFragment activitiesFragment) {
        mActivitiesFragment = activitiesFragment;
    }

    @Override
    public void onResume() {
        if (imageList.isEmpty())
            hideRecyclerView();
        super.onResume();
    }

    //    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_add_post, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog_background_7dp);
        setUpImagesRecyclerView();
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
        };

        int[] colors = new int[]{
                R.color.colorCyan200,
                Color.GRAY};

        ColorStateList colorStateList = new ColorStateList(
                states, colors);

        binding.addPostPostButton.setEnabled(false);
        binding.addPostPostButton.setSupportBackgroundTintList(colorStateList);
        binding.addPostPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                binding.addPostPostButton.setEnabled();
                if (getPost() != null)
                    mActivitiesFragment.addPost(getPost());
                dismiss();
            }
        });
        binding.addPostAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() <= 8) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else
                    Toast.makeText(getActivity(), "Maximum of 8 images required!",
                            Toast.LENGTH_SHORT).show();
            }
        });

        binding.addPostPromote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        if (getArguments() != null && getArguments().containsKey(BundleConstants.IMAGE_URL) &&
                getArguments().getParcelable(BundleConstants.IMAGE_URL) != null) {
            schoolLogoImage = getArguments().getParcelable(BundleConstants.IMAGE_URL);
            if (schoolLogoImage != null && !schoolLogoImage.getImageUrl().isEmpty())
                Picasso.get()
                        .load(schoolLogoImage.getImageUrl())
                        .placeholder(R.color.colorLightGrey)
                        .into(binding.addPostSchoolLogo);
        }

        binding.addPostBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    binding.addPostPostButton.setEnabled(true);
                    Log.e(TAG, charSequence.length() + " -------------------- ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return binding.getRoot();
    }

    /**
     * This method is called to get an instance of the post, initializes the required fields
     *
     * @return an instance of a post
     */
    private Post getPost() {
        Post post = null;
        if (!binding.addPostBody.getText().toString().isEmpty()) {
            post = new Post();

            post.setImageList(imageList);
            post.setSchoolLogo(schoolLogoImage);
            post.setBody(binding.addPostBody.getText().toString());
            post.setPromoted(binding.addPostPromote.isChecked());
        } else {
            Toast.makeText(getActivity(), "Post body cannot be empty!", Toast.LENGTH_SHORT).show();
        }

        return post;
    }

    /**
     * This method helps sets up the post images recyclerview
     */
    private void setUpImagesRecyclerView() {
//        binding.addPostImageRecyclerView.setVisibility(View.VISIBLE);
        postImagesAdapter = new PostAdapter.PostImagesAdapter(imageList, getActivity());
        binding.addPostImageRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false));
        binding.addPostImageRecyclerView.setAdapter(postImagesAdapter);
    }

    /**
     * If there are no images in the post, the recyclerview meant for it is hidden
     */
    private void hideRecyclerView() {
        binding.addPostImageRecyclerView.setVisibility(View.GONE);
        binding.postFieldRecyclerDivider.setVisibility(View.GONE);
    }

    /**
     * If there are no images in the post, the recyclerview meant for it is shown
     */
    private void showRecyclerView() {
        binding.addPostImageRecyclerView.setVisibility(View.VISIBLE);
        binding.postFieldRecyclerDivider.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    if (imageUri != null) {
                        if (postImagesAdapter != null) {
                            Image image = new Image();
                            image.setImageUrl(imageUri.toString());
                            imageList.add(image);
                            showRecyclerView();
                            postImagesAdapter.setImages(imageList);
//                            postImagesAdapter.addImage(image);
                        }
                    } else
                        Toast.makeText(getActivity(), "Error getting image", Toast.LENGTH_SHORT).show();

                }
        }
    }
}
