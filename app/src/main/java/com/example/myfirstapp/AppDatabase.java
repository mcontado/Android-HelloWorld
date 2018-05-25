package com.example.myfirstapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.myfirstapp.dao.UserSettingsDao;
import com.example.myfirstapp.entity.UserSettings;

@Database(entities = {UserSettings.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserSettingsDao userSettingsDao();
}

