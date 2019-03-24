package com.example.android.schoolfinder.normalUsers;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.normalUsers.Interfaces.SearchSchoolOfflineDatabaseCallback;
import com.example.android.schoolfinder.normalUsers.Utility.AppDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchSchoolViewModels extends AndroidViewModel {
    private DatabaseReference dbRef;
    private String mCountryChoice;
    private String mStateRegionChoice;
    private String mCityChoice;
    private String mSearchQuery;
    private List<String> mSchoolCategories;
    private MutableLiveData<School> schoolMutableLiveData = new MutableLiveData<>();
    private AppDatabase appDatabase;

    private List<School> schoolList = new ArrayList<>();
    private SearchSchoolOfflineDatabaseCallback offlineCallback;

    public SearchSchoolViewModels(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }

//    private Deserializer deserializer = new Deserializer();

    /**
     * This is used to initialize a mutable live data of the school item depending on the pos
     * to be used in the detail fragmnet
     *
     * @param pos position to get
     */
    public void initMasterDetailViewModel(int pos) {
        schoolMutableLiveData.setValue(schoolList.get(pos));
    }

    /**
     * This is to get a single school livedata object usinf the id
     * @param schoolId a string of the school id
     * @return livedata object of school
     */
    public LiveData<School> getSchoolLiveData(String schoolId) {

        SearchSchoolQueryLiveData.SingleSchoolQueryLiveData liveData =
                new SearchSchoolQueryLiveData.SingleSchoolQueryLiveData(schoolId);
        LiveData<School> schoolLiveData = Transformations.map(liveData, new SingleDeserializer());
        return schoolLiveData;
    }

    public LiveData<List<School>> getSchoolsLivedata(String country, String stateRegion, List<String> categories) {
        mCountryChoice = country;
        mStateRegionChoice = stateRegion;
        mSchoolCategories = categories;
        SearchSchoolQueryLiveData mLiveData = new SearchSchoolQueryLiveData(country, stateRegion);
        LiveData<List<School>> schoolLiveData =
                Transformations.map(mLiveData, new Deserializer());
        return schoolLiveData;
    }

    public LiveData<List<School>> getOfflineSchoolsLivedata(String country, String stateRegion, List<String> categories) {
        mCountryChoice = country;
        mStateRegionChoice = stateRegion;
        mSchoolCategories = categories;

        return appDatabase.schoolDao().getAllSchool(country, stateRegion);
//        return appDatabase.schoolDao().getAllSchool(country, stateRegion, categories);
    }

    public LiveData<List<School>> filterByCountryStateRegion(String country, String stateRegion) {
        mCountryChoice = country;
        mStateRegionChoice = stateRegion;
//        deserializer.addIdsFromStates(null);
        SearchSchoolQueryLiveData mLiveData = new SearchSchoolQueryLiveData(country, stateRegion);
        LiveData<List<School>> schoolLiveData =
                Transformations.map(mLiveData, new Deserializer());
        return schoolLiveData;
    }

    /**
     * This method helps filter the school list by the categories selected
     *
     * @param categories list of categories selected
     * @return livedata of the school
     */
    public LiveData<List<School>> filterByCategories(List<String> categories) {
        mSchoolCategories = categories;
//        deserializer = new Deserializer();
//        deserializer.filterSchoolsByCategories(null, true);
//        deserializer.filterIdListByCategories(null);
        SearchSchoolQueryLiveData mLiveData = new SearchSchoolQueryLiveData(this.mCountryChoice, this.mStateRegionChoice);
        LiveData<List<School>> schoolLiveData =
                Transformations.map(mLiveData, new Deserializer());
        return schoolLiveData;
    }


    public void setOfflineCallback(SearchSchoolOfflineDatabaseCallback offlineCallback) {

        this.offlineCallback = offlineCallback;
    }

    public LiveData<List<School>> getAllSchoolsOffline() {
        schoolList.clear();
        return appDatabase.schoolDao().getAllSchool();
    }

    public void insertSchool(School school) {
        new AddSchoolAsyncTask(appDatabase, offlineCallback)
                .execute(school);
    }

    public void deleteSchool(School school) {
        new DeleteSchoolAsyncTask(appDatabase, offlineCallback)
                .execute(school);
    }

    /**
     * To check if database exist
     *
     * @param id
     */
    public void getSchool(String id) {
        new GetSchoolByIdAsyncTask(appDatabase, offlineCallback)
                .execute(id);
    }

    private class SingleDeserializer implements Function<DataSnapshot, School> {
        @Override
        public School apply(DataSnapshot input) {
            School school = input.getValue(School.class);
            return school;
        }
    }

    private class Deserializer implements Function<DataSnapshot, List<School>> {
        private final String TAG = Deserializer.class.getSimpleName();
        private DataSnapshot dataSnapshot;
        private List<String> schoolsUidList = new ArrayList<>();

        @Override
        public List<School> apply(DataSnapshot input) {
            Log.e(TAG, "Deserializer's apply() called -------------- ");
            schoolList.clear();
            schoolsUidList.clear();
            dataSnapshot = input;

            if (mSchoolCategories != null && !mSchoolCategories.isEmpty()) {
                filterIdListByCategories(input);
                addIdsFromStates(input, true);
            } else {
                addIdsFromStates(input, false);
            }
            for (String s : schoolsUidList) {
                schoolList.add(input.child(s).child(FirebaseConstants.SCHOOL_DETAIL_NODE).getValue(School.class));
            }
            return schoolList;
        }

        /**
         * This method checks the stateRegion and country provided
         * then iterates over the uids found in them and add them to the schoolsUidsList variable
         *
         * @param snapshot of the school node
         */
        private void addIdsFromStates(DataSnapshot snapshot, boolean categoryCalledBefore) {
            List<String> schoolUidFromState = new ArrayList<>();

            if (mCountryChoice == null && mStateRegionChoice == null) return;
            if (snapshot == null) snapshot = dataSnapshot;
            DataSnapshot stateSnapShot = snapshot.child(FirebaseConstants.COUNTRIES)
                    .child(mCountryChoice)
                    .child(mStateRegionChoice);

            //loop through the list of snapshots in the state node
            for (DataSnapshot s : stateSnapShot.getChildren()) {
                //if category filter was not done before filtering by state then just add it to the list
                if (!categoryCalledBefore)
                    schoolsUidList.add(s.getKey());
                    //else add it to the list meant for uids gotten from state
                else
                    schoolUidFromState.add(s.getKey());
//                    schoolsUidList.add(s.getKey());

                Log.e(TAG, "*******************----- " + s.toString() + " -----****************");
            }
            //If filtering by category was done before filtering by state then from the list of school id
            //remove any that isn't contained in the state list
            if (categoryCalledBefore) {
                if (schoolsUidList != null && !schoolsUidList.isEmpty())
                    for (int i = 0; i <= schoolsUidList.size() - 1; i++) {
                        if (!schoolUidFromState.contains(schoolsUidList.get(i)))
                            schoolsUidList.remove(schoolsUidList.get(i));
                    }
            }

            Log.e(TAG, "*******************----- schoolsUidList " + schoolsUidList.toString() + " -----****************");

        }

        /**
         * This method checks the school Uid list that is populated by getting all uids in a city in a country
         * then iterates over the loop, check to see if the category key exist in the list, if yes pass over
         * else it removes that item from the list
         *
         * @param dataSnapshot of the school node
         */
        private void filterIdListByCategories(DataSnapshot dataSnapshot) {
            List<String> schoolsUidFromCategory = new ArrayList<>();
            if (mSchoolCategories == null || mSchoolCategories.isEmpty()) return;
//
            Iterable<DataSnapshot> categorySnapShots;
            //Iterate through the schoolCategories included
            for (String schoolCategory : mSchoolCategories) {
                //Get the snapshot list of the school categories
                categorySnapShots = dataSnapshot.child(FirebaseConstants.CATEGORIES_NODE)
                        .child(schoolCategory)
                        .getChildren();

                //Iterate over the category snapshots gotten

                for (DataSnapshot categorySnapshot : categorySnapShots) {
                    if (!schoolsUidFromCategory.contains(categorySnapshot.getKey()))
                        schoolsUidFromCategory.add(categorySnapshot.getKey());
                }
            }
            schoolsUidList.clear();
            schoolsUidList.addAll(schoolsUidFromCategory);
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

        public static class GetAllSchoolAsyncTask extends AsyncTask<Void, Void, List<School>>{
            private final AppDatabase appDatabase;
            private final SearchSchoolOfflineDatabaseCallback databaseCallback;

            public GetAllSchoolAsyncTask(AppDatabase appDatabase, SearchSchoolOfflineDatabaseCallback databaseCallback) {

                this.appDatabase = appDatabase;
                this.databaseCallback = databaseCallback;
            }

            @Override
            protected void onPostExecute(List<School> schools) {
                super.onPostExecute(schools);
                databaseCallback.schoolsGotten(schools);
            }

            @Override
            protected List<School> doInBackground(Void... voids) {

                return appDatabase.schoolDao().getAllSchoolNoLiveData();
            }
        }
    private static class GetSchoolByIdAsyncTask extends AsyncTask<String, Void, School> {

        private final AppDatabase appDatabase;
        private final SearchSchoolOfflineDatabaseCallback databaseCallback;

        public GetSchoolByIdAsyncTask(AppDatabase appDatabase, SearchSchoolOfflineDatabaseCallback databaseCallback) {

            this.appDatabase = appDatabase;
            this.databaseCallback = databaseCallback;
        }

        @Override
        protected void onPostExecute(School school) {
            super.onPostExecute(school);
            databaseCallback.schoolGotten(school);
        }

        @Override
        protected School doInBackground(String... strings) {
            return appDatabase.schoolDao().getSchoolById(strings[0]);
        }
    }

    static class DeleteSchoolAsyncTask extends AsyncTask<School, Void, Void> {
        private AppDatabase database;
        private SearchSchoolOfflineDatabaseCallback callback;

        public DeleteSchoolAsyncTask(AppDatabase database, SearchSchoolOfflineDatabaseCallback callback) {
            super();
            this.database = database;
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.schoolDeleted();
        }

        @Override
        protected Void doInBackground(School... schools) {
            database.schoolDao().deleteSchool(schools[0]);
            return null;
        }
    }

    private static class AddSchoolAsyncTask extends AsyncTask<School, Void, Void> {

        private final AppDatabase appDatabase;
        private final SearchSchoolOfflineDatabaseCallback callback;

        public AddSchoolAsyncTask(AppDatabase appDatabase, SearchSchoolOfflineDatabaseCallback callback) {

            this.appDatabase = appDatabase;
            this.callback = callback;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.schoolAdded();
        }

        @Override
        protected Void doInBackground(School... schools) {
            appDatabase.schoolDao().insertSchool(schools[0]);
            return null;
        }
    }
}
