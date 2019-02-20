package com.example.android.schoolfinder.normalUsers;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.School;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchSchoolViewModels extends ViewModel {
    private DatabaseReference dbRef;
    private String mCountryChoice;
    private String mStateRegionChoice;
    private String mCityChoice;
    private String mSearchQuery;
    private List<String> mSchoolCategories;

    private List<School> schoolList = new ArrayList<>();

    public SearchSchoolViewModels() {
        super();
    }

    private Deserializer deserializer = new Deserializer();

    public LiveData<List<School>> getSchoolsLivedata(String country, String stateRegion, List<String> categories) {
        mCountryChoice = country;
        mStateRegionChoice = stateRegion;
        mSchoolCategories = categories;
        SearchSchoolQueryLiveData mLiveData = new SearchSchoolQueryLiveData(country, stateRegion);
        LiveData<List<School>> schoolLiveData =
                Transformations.map(mLiveData, deserializer);
        return schoolLiveData;
    }

    public void filterByCountryStateRegion(String country, String stateRegion) {
        mCountryChoice = country;
        mStateRegionChoice = stateRegion;
        deserializer.addIdsFromStates(null);
    }

    public void filterByCategories(List<String> categories) {
        mSchoolCategories = categories;
//        deserializer.filterSchoolsByCategories(null, true);
        deserializer.filterIdListByCategories(null);
    }

    private class Deserializer implements Function<DataSnapshot, List<School>> {
        private final String TAG = Deserializer.class.getSimpleName();
        private DataSnapshot dataSnapshot;
        private List<String> schoolsUidList = new ArrayList<>();

        @Override
        public List<School> apply(DataSnapshot input) {
            schoolList.clear();
            dataSnapshot = input;
            if (mSchoolCategories != null && !mSchoolCategories.isEmpty()) {
//                filterSchoolsByCategories(input, true);
                addIdsFromStates(input);
            } else {
//                filterByCountryStateRegion(input, true);
                filterIdListByCategories(input);
            }
            for (String s : schoolsUidList) {
                schoolList.add(input.child(s).getValue(School.class));
            }
            return schoolList;
        }

        /**
         * This method checks the stateRegion and country provided
         * then iterates over the uids found in them and add them to the schoolsUidsList variable
         *
         * @param snapshot of the school node
         */
        private void addIdsFromStates(DataSnapshot snapshot) {
            if (mCountryChoice == null && mStateRegionChoice == null) return;
            if (snapshot == null) snapshot = dataSnapshot;
            DataSnapshot stateSnapShot = snapshot.child(FirebaseConstants.COUNTRIES)
                    .child(mCountryChoice)
                    .child(mStateRegionChoice);
            for (DataSnapshot s : stateSnapShot.getChildren()) {
                schoolsUidList.add(s.getKey());
            }

        }

        /**
         * This method checks the school Uid list that is populated by getting all uids in a city in a country
         * then iterates over the loop, check to see if the category key exist in the list, if yes pass over
         * else it removes that item from the list
         *
         * @param dataSnapshot of the school node
         */
        private void filterIdListByCategories(DataSnapshot dataSnapshot) {
            if (mSchoolCategories == null || mSchoolCategories.isEmpty()) return;
            if (dataSnapshot == null) dataSnapshot = this.dataSnapshot;
            Iterable<DataSnapshot> categorySnapShots;
            //Iterate through the schoolCategories included
            for (String schoolCategory : mSchoolCategories) {
                //Get the snapshot list of the school categories
                categorySnapShots = dataSnapshot.child(FirebaseConstants.CATEGORY)
                        .child(schoolCategory)
                        .getChildren();
                //Iterate over the category snapshots gotten
                for (DataSnapshot categorySnapshot : categorySnapShots) {
                    if (categorySnapshot.getValue() != null && categorySnapshot.getKey() != null) {
                        Log.e(TAG, "*******************----- " + categorySnapshot.toString() + " -----****************");
                        if (!schoolsUidList.contains(categorySnapshot.getKey()))
                            schoolsUidList.remove(categorySnapshot.getKey());
//                        schoolList.add(dataSnapshot.child(categorySnapshot.getKey()).getValue(School.class));
                    }
                }
            }
        }

        private Map<String, Object> filterByCountryStateRegion(DataSnapshot snapshot, boolean isAllowedToSetValue) {
            if (mCountryChoice == null && mStateRegionChoice == null) return new HashMap<>();
            if (snapshot == null) snapshot = dataSnapshot;
            schoolList.clear();
            Map<String, Object> schoolIds = new HashMap<>();
            for (DataSnapshot s : snapshot.getChildren()) {
                if (isAllowedToSetValue) schoolList.add(s.getValue(School.class));
                if (s.getKey() != null && s.getValue() != null)
                    schoolIds.put(s.getKey(), true);
            }

            return schoolIds;
        }

        /**
         * This method returns all the schools id in the "category" provided
         *
         * @param dataSnapshot        of the schools in a state
         * @param isAllowedToSetValue if its allowed to setValue
         * @return
         */
        private Map<String, Object> filterSchoolsByCategories(DataSnapshot dataSnapshot,
                                                              boolean isAllowedToSetValue) {
            if (mSchoolCategories == null || mSchoolCategories.isEmpty()) return null;
            if (dataSnapshot == null) dataSnapshot = this.dataSnapshot;
            Map<String, Object> schoolsIdInCategories = new HashMap<>();
            schoolList.clear();
//            schoolsIdInCategories.
            Iterable<DataSnapshot> categorySnapShots;
            //Iterate through the schoolCategories included
            for (String schoolCategory : mSchoolCategories) {
                //Get the snapshot list of the school categories
                categorySnapShots = dataSnapshot.child(FirebaseConstants.CATEGORY)
                        .child(schoolCategory)
                        .getChildren();
                //Iterate over the category snapshots gotten
                for (DataSnapshot categorySnapshot : categorySnapShots) {
                    if (categorySnapshot.getValue() != null && categorySnapshot.getKey() != null) {
                        Log.e(TAG, "*******************----- " + categorySnapshot.toString() + " -----****************");
                        //Put the id's found in the map
                        schoolsIdInCategories.put(categorySnapshot.getKey(), categorySnapshot.getValue());
                        if (isAllowedToSetValue)
                            schoolList.add(dataSnapshot.child(categorySnapshot.getKey()).getValue(School.class));
                    }
                }
            }
            //Return the map containing all the uids of school in the category of schools provided
            return schoolsIdInCategories;
        }
    }
}
