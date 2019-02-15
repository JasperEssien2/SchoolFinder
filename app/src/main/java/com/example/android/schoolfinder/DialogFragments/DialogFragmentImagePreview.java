package com.example.android.schoolfinder.DialogFragments;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.Constants.BundleConstants;
import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.DialogFragmentImagePreviewBinding;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.squareup.picasso.Picasso;

public class DialogFragmentImagePreview extends DialogFragment {
    private DialogFragmentImagePreviewBinding mImagePreviewBinding;
    private MediaStorageCallback mStorageCallback;
    private MediaStorage mMediaStorage;

    public static DialogFragmentImagePreview newInstance(MediaStorageCallback storageCallback, Uri uri,
                                                         int action) {
        DialogFragmentImagePreview dialogFragmentImagePreview = new DialogFragmentImagePreview();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mediaCallback", storageCallback);
        bundle.putString("uri", uri.toString());
        bundle.putInt("action", action);
        dialogFragmentImagePreview.setArguments(bundle);
        return dialogFragmentImagePreview;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mImagePreviewBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_image_preview,
                container, false);
        if (getArguments() != null && getArguments().containsKey("mediaCallback")) {
            final Uri uri = Uri.parse(getArguments().getString("uri"));
            final int action = getArguments().getInt("action");

            Picasso
                    .get()
                    .load(uri)
//                    .centerCrop()
                    .into(mImagePreviewBinding.imageView);
            mStorageCallback = (MediaStorageCallback) getArguments().getSerializable("mediaCallback");

            mMediaStorage = new MediaStorage(mStorageCallback);

            mImagePreviewBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            mImagePreviewBinding.okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (action) {
                        case BundleConstants
                                .ACTION_STORE_PROFILE_IMAGE:
                            mMediaStorage.addProfileImage(true, uri);
                            break;
                        case BundleConstants
                                .ACTION_STORE_LOGO:
                            mMediaStorage.addSchoolLogo(uri);
                            break;

                    }
                }
            });
        }
        return mImagePreviewBinding.getRoot();
    }


}
