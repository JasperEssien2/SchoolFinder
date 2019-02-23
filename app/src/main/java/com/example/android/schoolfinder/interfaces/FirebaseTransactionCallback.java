package com.example.android.schoolfinder.interfaces;

import com.example.android.schoolfinder.Models.Post;
import com.example.android.schoolfinder.Models.School;

public interface FirebaseTransactionCallback {

    void post(Post post, boolean isSuccessful);

    void following(School school, boolean isSuccessful);

    void impressedExpression(School school, boolean isSuccessful);

    void notImpressedExpression(School school, boolean isSuccessful);

    void neutralExpression(School school, boolean isSuccessful);

    void postLike(Post post, boolean isSuccessful);
}
