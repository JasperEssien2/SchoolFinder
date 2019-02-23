package com.example.android.schoolfinder.FirebaseHelper;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.interfaces.MediaStorageCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class MediaStorage {

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
    public void addSchoolImages(Uri uri, final String tag) {
        StorageReference ref = null;

        ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.STORAGE_SCHOOL_IMAGES)
                .child(tag);
        final StorageReference finalRef = ref;
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
                    mStorageCallback.schoolImageAdded(task.getResult().toString());
                else mStorageCallback.schoolImageAdded(null);
            }
        });
    }

    public void addOtherImages() {

    }

    public void addPostImages(List<Image> imageList) {

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
