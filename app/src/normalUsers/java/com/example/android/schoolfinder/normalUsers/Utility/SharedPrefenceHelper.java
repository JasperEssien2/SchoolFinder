package com.example.android.schoolfinder.normalUsers.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefenceHelper {
    public static final String SHARED_PREF_NAME = "FilterPref";
    public static final String CATEGORY_PREF = "Categories";
    public static final String COUNTRY_PREF = "Country";
    public static final String STATE_PREF = "State";

    private Activity sActivity;
    private static SharedPreferences sSharedPreferences;

    public SharedPrefenceHelper(Activity activity) {

        sActivity = activity;
        sSharedPreferences = sActivity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setCategoriesSharedPref(List<String> categories) {
        if (categories == null) return;
        if (categories.isEmpty()) return;

        Set<String> categorySet = new HashSet<String>(categories);
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putStringSet(CATEGORY_PREF, categorySet);
        editor.apply();
    }

    public static List<String> getCategoriesSharedPrefValue() {
        return new ArrayList<>(sSharedPreferences.getStringSet(CATEGORY_PREF, new HashSet<String>()));
    }

    public static void setCountryStateSharedPref(String country, String state){
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(COUNTRY_PREF, country);
        editor.putString(STATE_PREF, state);
        editor.apply();
    }

    public String[] getCountryStateSharedPrefValue(){
        String[] strings = new String[2];
        strings[0] = sSharedPreferences.getString(COUNTRY_PREF, "");
        strings[1] = sSharedPreferences.getString(STATE_PREF, "");
        return strings;
    }
}
