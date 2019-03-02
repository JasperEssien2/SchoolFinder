package com.example.android.schoolfinder.FirebaseHelper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaStorage {

    private static final String TAG = MediaStorage.class.getSimpleName();
    private MediaStorageCallback mStorageCallback;
    private StorageReference mStorageRef;


    public MediaStorage(MediaStorageCallback callback) {

        mStorageCallback = callback;
        mStorageRef = FirebaseStorage
                .getInstance().getReference();
    }

    /**
     * This method is called to add profile image to firebase storage
     *
     * @param isSchool to check whether its a school owner profile image or not
     * @param uri      the uri of the image to be inserted to firebase storage
     */
    public void addProfileImage(boolean isSchool, Uri uri) {
        StorageReference ref = null;

        if (!isSchool) {
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_PROFILE);

        } else {
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_SCHOOL_OWNER_PROFILE);
        }

        final StorageReference finalRef = ref;
        ref.putFile(uri)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                            return finalRef.getDownloadUrl();
                        return null;
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null)
                        mStorageCallback.profileImageStored(task.getResult().toString(), true);
                    else mStorageCallback.profileImageStored(null, false);
                }
            }
        });
    }

    /**
     * This method is called to add an image of the school logo to firebase storage
     *
     * @param uri the uri of the image
     */
    public void addSchoolLogo(Uri uri) {
        StorageReference ref = null;

        ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.STORAGE_LOGO);
        try {
            final StorageReference finalRef = ref;
            ref.putFile(uri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful())
                                throw task.getException();
                            return finalRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        mStorageCallback.logoStored(task.getResult().toString());
                    } else mStorageCallback.logoStored(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called to add an image of certification and achievement of the school to firebase storage
     * in the right node
     *
     * @param isCertificate checks whether the image to be inserted is for certificate or achievements
     * @param cert          an instance of Certificate so as to be stored in the database as part of the school info
     * @param uri           the uri of the image to be stored in firebase storage
     */
    public void addCerticficatesImage(final boolean isCertificate, final Certificate cert, Uri uri) {
        StorageReference ref = null;

        if (isCertificate) {
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_CERT_IMAGE)
                    .child(cert.getName());
        } else ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.STORAGE_ACHIEVEMENT_IMAGE)
                .child(cert.getName());

        try {
            final StorageReference finalRef = ref;
            ref.putFile(uri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return finalRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                        mStorageCallback.certificateImageStored(cert, task.getResult().toString(), isCertificate);
                    else mStorageCallback.certificateImageStored(null, null, isCertificate);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called to add school images to the storage
     *
     * @param uri the uri of the image
     * @param tag the image tag
     */
    public void addSchoolImages(Uri uri, String tag, String newTag) {
        StorageReference ref = null;

        if (tag != null && newTag != null) {
            mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_SCHOOL_IMAGES)
                    .child(tag).delete();
            tag = newTag;
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_SCHOOL_IMAGES)
                    .child(tag);
        } else if (tag != null)
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_SCHOOL_IMAGES)
                    .child(tag);

        else
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_SCHOOL_IMAGES);

        final StorageReference finalRef = ref;
        final String tag_ = tag;
        ref.putFile(uri)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) throw task.getException();
                        return finalRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                    mStorageCallback.schoolImageAdded(task.getResult().toString(), tag_);
                else mStorageCallback.schoolImageAdded(null, tag_);
            }
        });
    }

    public void addOtherImages() {

    }

    private int successCount = 0;

    public void addPostImages(final Post p, final List<Image> imageList) {
        final StorageReference ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.POSTS_NODE)
                .child(p.getUid());
        final Map<String, String> imageUrlsMap = new HashMap<>();
        Map<String, UploadTask> uploadTaskMap = new HashMap<>();

        for (Image image : imageList) {
            Log.e(TAG, "image ---- " + image.toString());
            uploadTaskMap.put(image.getId(), ref.putFile(Uri.parse(image.getImageUrl())));
        }

        List<UploadTask> uploads = new ArrayList<>(uploadTaskMap.values());
        final List<String> keys = new ArrayList<>(uploadTaskMap.keySet());
        Log.e(TAG, "upload task keys ---- " + keys.toString());
        Log.e(TAG, "upload task values ---- " + uploads.toString());
        for (int i = 0; i <= uploadTaskMap.keySet().size() - 1; i++) {
            final int finalI = i;
            uploads.get(finalI).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        successCount++;
                        Log.e(TAG, "Final I count ----- " + finalI);
                        imageUrlsMap.put(keys.get(finalI), task.getResult().toString());
                        Log.e(TAG, "ImageUrlsMap ----- " + imageUrlsMap.toString());
                        if (successCount == imageList.size()) {
                            for (int i = 0; i <= imageList.size() - 1; i++) {
                                if (imageUrlsMap.containsKey(imageList.get(i).getId()))
                                    imageList.get(i).setImageUrl(imageUrlsMap.get(imageList.get(i).getId()));
                            }

                            p.setImageList(imageList);
                            mStorageCallback.postImageAdded(p, true);
                        } else {
                            mStorageCallback.postImageAdded(p, false);
                        }
                    }

                }
            });
        }
    }

    /**
     * This method is called to delete profile pics in firebase storage
     *
     * @param isSchool if its a school owner pic or not
     */
    public void deleteProfilePics(boolean isSchool) {
        StorageReference ref = null;

        if (!isSchool) {
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_PROFILE);

        } else {
            ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(FirebaseConstants.STORAGE_SCHOOL_OWNER_PROFILE);
        }

        ref.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) mStorageCallback.profileImageDeleted(true);
                        else mStorageCallback.profileImageDeleted(false);
                    }
                });
    }
}
