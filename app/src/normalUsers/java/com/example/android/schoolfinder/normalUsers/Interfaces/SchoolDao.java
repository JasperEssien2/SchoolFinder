package com.example.android.schoolfinder.normalUsers.Interfaces;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.schoolfinder.Models.School;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SchoolDao {
    @Insert(onConflict = REPLACE)
    void insertSchool(School school);

    @Delete
    void deleteSchool(School school);

    @Query("SELECT * FROM schools")
    LiveData<List<School>> getAllSchool();

    @Query("SELECT * FROM schools")
    List<School> getAllSchoolNoLiveData();

    @Query("SELECT * FROM schools WHERE country LIKE :country AND state_region LIKE " +
            ":state_region AND schoolCategory LIKE :category")
    LiveData<List<School>> getAllSchool(String country, String state_region, List<String> category);

    @Query("SELECT * FROM schools WHERE schoolName LIKE :name")
    LiveData<List<School>> searchByName(String name);

    @Query("SELECT * FROM schools WHERE country LIKE :country AND state_region LIKE :state")
    LiveData<List<School>> sortByCountryState(String country, String state);

    @Query("SELECT * FROM schools WHERE schoolCategory LIKE :categories")
    LiveData<List<School>> sortByCategory(List<String> categories);

    @Query("SELECT * FROM schools WHERE id LIKE :id")
    School getSchoolById(String id);
}
