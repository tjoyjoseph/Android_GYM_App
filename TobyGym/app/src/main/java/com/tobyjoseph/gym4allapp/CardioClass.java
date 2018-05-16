package com.tobyjoseph.gym4allapp;

/**
 * Created by Toby Joseph on 15/03/2018.
 */

public class CardioClass {

    public String ActivityType;
    public int CaloriesBurned;
    public String Date;

    CardioClass(){}

    public CardioClass(String activityType, int caloriesBurned, String date) {
        ActivityType = activityType;
        CaloriesBurned = caloriesBurned;
        this.Date = date;
    }
}
