package com.tobyjoseph.gym4allapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Toby Joseph on 21/03/2018.
 */

public class FileHandling {

    //// code from this site was used for file handling - https://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android

    private static FileHandling fileHandling = new FileHandling();

    private static String filename = "userData.txt";

    private FileHandling(){}


    private static String objectToJSON(User user){

        GsonBuilder gsonBuilder  = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        Gson gson = gsonBuilder.create();
        String userJSON = gson.toJson(user);
        System.out.println(userJSON);

        return userJSON;
    }

    private static User jsonToObject(String jsonText, User user){

        GsonBuilder gsonBuilder  = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonText,user.getClass());

    }

    public static void saveData(Context context, Boolean empty){
        Log.d("Toby","File handling save data");
        String dataToBeSaved = " ";
        if (empty == false){
            dataToBeSaved = objectToJSON(User.user);
        }
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(dataToBeSaved);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    public static void retrieveData(Context context){
        Log.d("Toby","File handling retrieve from data");

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder userDataBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    userDataBuilder.append(receiveString);
                }

                inputStream.close();
                if(!(userDataBuilder.toString().equals(" ")))
                    User.user = jsonToObject(userDataBuilder.toString(),User.user);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


    }

}
