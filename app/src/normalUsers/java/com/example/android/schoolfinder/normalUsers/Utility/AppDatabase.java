package com.example.android.schoolfinder.normalUsers.Utility;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.schoolfinder.Models.School;
import com.example.android.schoolfinder.normalUsers.Interfaces.SchoolDao;

@Database(entities = {School.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context){
        if(INSTANCE == null)
            INSTANCE = Room
                    .databaseBuilder(context, AppDatabase.class, "school_db")
                    .build();
        return INSTANCE;
    }

    public abstract SchoolDao schoolDao();
}
