package com.tobyjoseph.gym4allapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Toby Joseph on 10/03/2018.
 */

//// https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/ used to convert from Object to Json and vise versa

final class User {

    public static String email  = "";
    public static float weightKG = 0;
    public static float heightCM=0;
    public static float BFat=0;
    public static char sex='M';
    public static int year=0;
    public static int month=0;
    public static int day=0;

    public static ArrayList<CardioClass> cardio = new ArrayList<>();
    public static ArrayList<StrengthClass> strength = new ArrayList<>();
    public static ArrayList<PedometerClass> steps = new ArrayList<>();
    //private static Context context;

    public static User user = new User();

    public User() {
    }

    public static int calculateBMR()
    {
        Calendar dateOfYourBirth = new GregorianCalendar(year, month, day);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dateOfYourBirth.get(Calendar.YEAR);
        dateOfYourBirth.add(Calendar.YEAR, age);
        if (today.before(dateOfYourBirth)) {
            age--;
        }
        int BMR = 0;

        if (sex == 'M') {
            BMR = (int) (10 * weightKG + 6.25 * heightCM - 5 * age + 5);
        } else {
            BMR = (int) (10 * weightKG + 6.25 * heightCM - 5 * age - 161);
        }
        return BMR;
    }

    public static void deleteDetails()
    {
        Log.d("Toby","Delete Singleton Class Details");
        //email = "";
        weightKG = 0;
        heightCM=0;
        BFat=0;
        sex='M';
        year=0;
        month=0;
        day=0;
        cardio.clear();
        strength.clear();
        steps.clear();
    }
}
