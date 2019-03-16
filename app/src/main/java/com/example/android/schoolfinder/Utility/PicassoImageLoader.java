package com.example.android.schoolfinder.Utility;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader {

    public PicassoImageLoader(Activity activity, final String imageUrl,
                              final int placeholder, final int errorImage, final ImageView imageView) {
        if (activity != null)
            Glide
                    .with(activity)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(placeholder)
                    .error(errorImage)
                    .into(imageView);

//        Picasso.get()
//                .load(imageUrl)
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(imageView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Picasso.get()
//                                .load(imageUrl)
//                                .placeholder(placeholder)
//                                .error(errorImage)
//                                .into(imageView, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(Exception e) {
//                                        Log.v("Picasso", "Could not fetch image");
//                                    }
//                                });
//                    }
//                });
    }

    public PicassoImageLoader(Context context, final String imageUrl,
                              final int placeholder, final int errorImage, final ImageView imageView) {
        if (context != null)
            Glide
                    .with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(placeholder)
                    .error(errorImage)
                    .into(imageView);

//        Picasso.get()
//                .load(imageUrl)
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(imageView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Picasso.get()
//                                .load(imageUrl)
//                                .placeholder(placeholder)
//                                .error(errorImage)
//                                .into(imageView, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(Exception e) {
//                                        Log.v("Picasso", "Could not fetch image");
//                                    }
//                                });
//                    }
//                });
    }

    public PicassoImageLoader(Activity activity, @DrawableRes final int imageResource,
                              final int placeholder, final int errorImage, final ImageView imageView) {
//        Glide
//                .with(activity)
//                .load(imageResource)
//                .centerCrop()
//                .placeholder(placeholder)
//                .error(errorImage)
//                .into(imageView);

        Picasso.get()
                .load(imageResource)
                .placeholder(placeholder)
                .error(errorImage)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Picasso", "Could not fetch image---- error " + e.getMessage());
                    }
                });
    }
}
