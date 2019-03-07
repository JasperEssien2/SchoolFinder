package com.example.android.schoolfinder.normalUsers.Interfaces;

import android.arch.lifecycle.LiveData;

import com.example.android.schoolfinder.Models.School;

import java.util.List;

public interface SearchSchoolOfflineDatabaseCallback {

    void schoolAdded();

    void schoolDeleted();

    void schoolGotten(School school);

    void schoolsGotten(LiveData<List<School>> listLiveData);

    void schoolsGotten(List<School> schools);
}
