package com.example.myfirstapp.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class UserSettings {

    @PrimaryKey
    @NonNull
    private String email;

    private String dailyMatchesReminderTime;

    private int maxDistanceSearchInMiles;

    private String gender;

    private boolean isPrivateAccount;

    private String ageRange;

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getDailyMatchesReminderTime() {
        return dailyMatchesReminderTime;
    }

    public void setDailyMatchesReminderTime(String dailyMatchesReminderTime) {
        this.dailyMatchesReminderTime = dailyMatchesReminderTime;
    }

    public int getMaxDistanceSearchInMiles() {
        return maxDistanceSearchInMiles;
    }

    public void setMaxDistanceSearchInMiles(int maxDistanceSearchInMiles) {
        this.maxDistanceSearchInMiles = maxDistanceSearchInMiles;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isPrivateAccount() {
        return isPrivateAccount;
    }

    public void setPrivateAccount(boolean privateAccount) {
        isPrivateAccount = privateAccount;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

}
