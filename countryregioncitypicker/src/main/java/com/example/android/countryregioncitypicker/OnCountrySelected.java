package com.example.android.countryregioncitypicker;

import com.example.android.countryregioncitypicker.Models.Country;

/**
 * Call back interface to be called when a county is selected in the dialog
 */
public interface OnCountrySelected {

    void countrySelected(Country country);
}
