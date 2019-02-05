package com.example.android.schoolfinder.Utility;


import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by JAHSWILL on 2/2/2019.
 */

public class Validation {
    public static final String EMAIL_NOT_VALID = "Enter a valid email";
    public static final String PASSWORD_LESS = "Password must be greater than six characters";
    public static final String EMPTY_FIELD = "Field cannot be empty";
    public static final String PASSWORD_NOT_EQUAL = "Password does not match";

    public static boolean validateFields(String name) {

        return !TextUtils.isEmpty(name);
    }

    public static boolean validateEmail(String string) {

        return !(TextUtils.isEmpty(string) || !Patterns.EMAIL_ADDRESS.matcher(string).matches());
    }

    public static boolean ValidateUrl(String string) {
        return !(TextUtils.isEmpty(string) || !Patterns.WEB_URL.matcher(string).matches());
    }
}

