package com.example.myfirstapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myfirstapp.entity.UserSettings;

import java.util.List;


@Dao
public interface UserSettingsDao {

//    @Query("SELECT * FROM usersettings where email = :email")
//    UserSettings getUserSettings(String email);
    @Query("SELECT * FROM usersettings WHERE email IN (:emails)")
    List<UserSettings> loadAllByIds(String[] emails);

    @Update
    void updateUserSettings(UserSettings... userSettings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserSettings(UserSettings... userSettings);
}
