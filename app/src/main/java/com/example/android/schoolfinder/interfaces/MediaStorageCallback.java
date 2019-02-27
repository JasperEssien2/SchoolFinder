package com.example.android.schoolfinder.interfaces;

import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Image;
import com.example.android.schoolfinder.Models.Post;

import java.util.List;

public interface MediaStorageCallback{

    void profileImageStored(String imageUrl, boolean isSuccesful);

    void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert);

    void profileImageDeleted(boolean isSuccessful);

    void logoStored(String imageUrl);

    void schoolImageAdded(String imageUrl, String tag);

    void postImageAdded(Post post, List<Image> images);
}
