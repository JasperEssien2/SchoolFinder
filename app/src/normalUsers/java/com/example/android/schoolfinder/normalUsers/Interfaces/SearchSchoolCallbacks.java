package com.example.android.schoolfinder.normalUsers.Interfaces;

import com.example.android.schoolfinder.Models.School;

import java.util.List;

public interface SearchSchoolCallbacks {

    void schoolsFound(List<School> schools);
}
