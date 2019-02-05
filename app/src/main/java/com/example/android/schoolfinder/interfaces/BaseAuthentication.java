package com.example.android.schoolfinder.interfaces;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.Models.Users;

public interface BaseAuthentication {

    void createNewUser(String email, String password);

    void putNewUserInDb(School school);

    void putNewUserInDb(Users user);

    void signIn(String email, String password);

    void logout();

    void deleteAccount(Users users);

    void deleteAccount(School school);
}
