package com.example.android.schoolfinder.schoolOwners.DialogFragments;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.schoolfinder.FirebaseHelper.MediaStorage;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.R;
import com.example.android.schoolfinder.databinding.DialogFragmentAddCertBinding;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.squareup.picasso.Picasso;

public class AddCertDialogFragment extends DialogFragment {

    private DialogFragmentAddCertBinding mAddCertBinding;
    private MediaStorageCallback mStorageCallback;
    private MediaStorage mMediaStorage;
    private Bitmap mBitmap = null;

    public static AddCertDialogFragment newInstance(MediaStorageCallback storageCallback, Uri uri,
                                                    int action, boolean isCert) {
        AddCertDialogFragment addCertDialogFragment = new AddCertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mediaCallback", storageCallback);
        bundle.putString("uri", uri.toString());
        bundle.putInt("action", action);
        bundle.putBoolean("isCert", isCert);
        addCertDialogFragment.setArguments(bundle);
        return addCertDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAddCertBinding = DataBindingUtil
                .inflate(inflater, R.layout.dialog_fragment_add_cert, container, false);
        if (getArguments() != null && getArguments().containsKey("mediaCallback")) {
            final Uri uri = Uri.parse(getArguments().getString("uri"));
            final int action = getArguments().getInt("action");
            final boolean isCert = getArguments().getBoolean("isCert");

            Picasso
                    .get()
                    .load(uri)
//                    .centerCrop()
                    .into(mAddCertBinding.imageView);
            mStorageCallback = (MediaStorageCallback) getArguments().getSerializable("mediaCallback");

            mMediaStorage = new MediaStorage(mStorageCallback);

            mAddCertBinding.addImageOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Certificate certificate = new Certificate();
                    certificate.setName(mAddCertBinding.certName.getText().toString());
                    certificate.setDescription(mAddCertBinding.certDescription.getText().toString());
//                    certificate.setImageOfCert(new Image());
                    mMediaStorage
                            .addCerticficatesImage(isCert, certificate, uri);
                }
            });

            mAddCertBinding.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        return mAddCertBinding.getRoot();
    }

}
