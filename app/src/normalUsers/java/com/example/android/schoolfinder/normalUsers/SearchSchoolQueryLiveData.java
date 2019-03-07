package com.example.android.schoolfinder.normalUsers;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.android.schoolfinder.Constants.FirebaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchSchoolQueryLiveData extends LiveData<DataSnapshot> {

    private static final String TAG = SearchSchoolQueryLiveData.class.getSimpleName();
    //    private String mCountryChoice;
//    private String mStateRegionChoice;
//    private String mCityChoice;
//    private String mSearchQuery;
//    private List<String> mSchoolCategories;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SCHOOLS_USERS_NODE);
    private Query query;
    private final MyValueListener listener = new MyValueListener();
    private Handler handler = new Handler();
    private boolean listenerRemovePending = false;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovePending = false;
        }
    };

    public SearchSchoolQueryLiveData(String countryChoice, String stateRegionChoice) {

//        mCountryChoice = countryChoice;
//        mStateRegionChoice = stateRegionChoice;
//        dbRef = dbRef.child(countryChoice).child(stateRegionChoice);
        query = dbRef;

    }

//    public void setValues(String countryChoice, String stateRegionChoice, String cityChoice, String searchQuery, List<String> schoolTypes) {
//        mCountryChoice = countryChoice;
//        mStateRegionChoice = stateRegionChoice;
//        mCityChoice = cityChoice;
//        mSearchQuery = searchQuery;
//        mSchoolCategories = schoolTypes;
//        if (mCityChoice != null && mSearchQuery == null &&
//                (mSchoolCategories != null && !mSchoolCategories.isEmpty())) {
//        }
//        //If only country choice is not null
////        else if(mCountryChoice != null && mStateRegionChoice == null && mCityChoice == null && mSearchQuery == null &&
////            mSchoolCategories == null){
////
////        }
//        //countryChoice and state/region choice is not null
//        else if (mCityChoice == null && mSearchQuery == null &&
//                mSchoolCategories == null) {
//
//
//        }//countryChoice and state/region choice and city choice is not null
//        else if (mCityChoice != null && mSearchQuery == null &&
//                mSchoolCategories == null) {
//            dbRef
//                    .child("Cities")
//                    .child(mCountryChoice)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            dataSnapshot.
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//        }
//
//        //countryChoice and state/region choice and city choice and search query is not null
//        else if (mCountryChoice != null && mStateRegionChoice != null && mCityChoice != null && mSearchQuery != null &&
//                mSchoolCategories == null) {
//
//        } else if (mCountryChoice == null && mSearchQuery != null && mSchoolCategories != null && !mSchoolCategories.isEmpty()) {
//
//        } else if (mCountryChoice == null && mSearchQuery != null && mSchoolCategories == null) {
//
//        }
//    }
//
//    public void filter() {
//
//    }

    public void countryStateRegionChanged(String countryChoice, String stateRegionChoice) {
//        mCountryChoice = countryChoice;
//        mStateRegionChoice = stateRegionChoice;
//        dbRef = dbRef.child(countryChoice).child(stateRegionChoice);
//        query = dbRef;
//        query.removeEventListener(listener);
//        query.addValueEventListener(listener);
//        if(mSchoolCategories != null && !mSchoolCategories.isEmpty())
//            listener.filterSchoolsByCategories(null, true);
//        else listener.filterByCountryStateRegion(null, true);
    }

//    public void categoriesValuesChanges(List<String> categories){
//        mSchoolCategories = categories;
//        listener.filterSchoolsByCategories(null, true);
//    }

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else {
            query.addValueEventListener(listener);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    private class MyValueListener implements ValueEventListener {


        private final String TAG = MyValueListener.class.getSimpleName();

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    /**
     * Livedata for a particular school node, using the id passed
     */
    public static class SingleSchoolQueryLiveData extends LiveData<DataSnapshot> {

        private static final String TAG = SingleSchoolQueryLiveData.class.getSimpleName();
        private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SCHOOLS_USERS_NODE);
        private Query query;
        private final MyValueListener listener = new MyValueListener();
        private Handler handler = new Handler();
        private boolean listenerRemovePending = false;
        private final Runnable removeListener = new Runnable() {
            @Override
            public void run() {
                query.removeEventListener(listener);
                listenerRemovePending = false;
            }
        };

        public SingleSchoolQueryLiveData(String id) {
            query = dbRef.child(id).child(FirebaseConstants.SCHOOL_DETAIL_NODE);

        }

        @Override
        protected void onActive() {
            if (listenerRemovePending) {
                handler.removeCallbacks(removeListener);
            } else {
                query.addValueEventListener(listener);
            }
            listenerRemovePending = false;
        }

        @Override
        protected void onInactive() {
            handler.postDelayed(removeListener, 2000);
            listenerRemovePending = true;
        }

        private class MyValueListener implements ValueEventListener {


            private final String TAG = MyValueListener.class.getSimpleName();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setValue(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }
    }
}
