package com.example.android.schoolfinder.interfaces;

import com.example.android.schoolfinder.Models.Certificate;
import com.example.android.schoolfinder.Models.Post;

public interface MediaStorageCallback{

    void profileImageStored(String imageUrl, boolean isSuccesful);

    void certificateImageStored(Certificate certificate, String imageUrl, boolean isCert);

    void profileImageDeleted(boolean isSuccessful);

    void logoStored(String imageUrl);

    void schoolImageAdded(String imageUrl, String tag);

    void postImageAdded(Post post, boolean isSuccessful);
}
