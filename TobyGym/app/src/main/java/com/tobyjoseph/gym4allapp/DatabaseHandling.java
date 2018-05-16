package com.tobyjoseph.gym4allapp;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Toby Joseph on 23/03/2018.
 */

    //Refrences
    //https://www.youtube.com/watch?v=jEmq1B1gveM
    //https://www.youtube.com/watch?v=EM2x33g4syY&t=22s

public class DatabaseHandling {

    ///Database Stuff
    private static FirebaseDatabase database;
    public static DatabaseReference ref;

    private static DatabaseHandling databaseHandling = new DatabaseHandling();

    DatabaseHandling(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
    }

    private static  String hashEmail() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String input = User.email;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(input.getBytes("UTF-8"));
        byte[] hash = digest.digest();

        return hash.toString();
    }



    public static boolean retrieveFromDatabase() {
        final int idinInt = User.email.hashCode();
        final String id = String.valueOf(idinInt).substring(1);
        Log.d("Toby",User.email+ " ID is "+id);
        Log.d("Toby","Retrive From Database");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists()) {
                    if (dataSnapshot.child(id).child("Email").exists()) {
                        User.email = dataSnapshot.child(id).child("Email").getValue(String.class);
                        User.weightKG = dataSnapshot.child(id).child("Weight").getValue(Float.class);
                        User.heightCM = dataSnapshot.child(id).child("Height").getValue(Float.class);
                        User.BFat = dataSnapshot.child(id).child("BodyFat").getValue(Float.class);
                        User.year = dataSnapshot.child(id).child("year").getValue(int.class);
                        User.month = dataSnapshot.child(id).child("Month").getValue(int.class);
                        User.day = dataSnapshot.child(id).child("Day").getValue(int.class);
                        User.sex = dataSnapshot.child(id).child("Sex").getValue(String.class).charAt(0);

                        if (dataSnapshot.child(id).child("Cardio").exists()) {
                            ArrayList<CardioClass> tempCardio = new ArrayList<>();
                            for (int i = 0; i < dataSnapshot.child(id).child("Cardio").getChildrenCount(); i++) {
                                if (dataSnapshot.child(id).child("Cardio").exists()) {
                                    System.out.print(i);
                                    tempCardio.add(dataSnapshot.child(id).child("Cardio").child(String.valueOf(i)).getValue(CardioClass.class));
                                }
                            }
                            User.cardio = tempCardio;

                        }
                        if (dataSnapshot.child(id).child("Strength").exists()) {
                            ArrayList<StrengthClass> tempstrength = new ArrayList<>();
                            for (int i = 0; i < dataSnapshot.child(id).child("Strength").getChildrenCount(); i++) {
                                if (dataSnapshot.child(id).child("Strength").child(String.valueOf(i)).exists()) {
                                    System.out.print(i);
                                    tempstrength.add(dataSnapshot.child(id).child("Strength").child(String.valueOf(i)).getValue(StrengthClass.class));
                                }
                            }
                            User.strength = tempstrength;
                        }

                        if (dataSnapshot.child(id).child("Steps").exists()) {
                            ArrayList<PedometerClass> tempSteps = new ArrayList<>();
                            for (int i = 0; i < dataSnapshot.child(id).child("Steps").getChildrenCount(); i++) {
                                if (dataSnapshot.child(id).child("Steps").child(String.valueOf(i)).exists()) {
                                    System.out.print(i);
                                    tempSteps.add(dataSnapshot.child(id).child("Steps").child(String.valueOf(i)).getValue(PedometerClass.class));
                                }
                            }
                            User.steps = tempSteps;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;

    }


    public static void saveToFirebase() {
        final int idinInt = User.email.hashCode();
        final String id = String.valueOf(idinInt).substring(1);;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Toby","Save To Database");

                ref.child(id).child("Email").setValue(User.email);
                ref.child(id).child("Weight").setValue(User.weightKG);
                ref.child(id).child("Height").setValue(User.heightCM);
                ref.child(id).child("BodyFat").setValue(User.BFat);
                ref.child(id).child("year").setValue(User.year);
                ref.child(id).child("Month").setValue(User.month);
                ref.child(id).child("Day").setValue(User.day);
                ref.child(id).child("Sex").setValue(String.valueOf(User.sex));

                if (User.cardio.isEmpty() != true) {
                    for (int i = 0; i < User.cardio.size(); i++) {

                        ref.child(id).child("Cardio").child(String.valueOf(i)).setValue(User.cardio.get(i));
                    }
                }
                if (User.strength.isEmpty() != true) {
                    StrengthClass tempStrenth;
                    for (int i = 0; i < User.strength.size(); i++) {
                        ref.child(id).child("Strength").child(String.valueOf(i)).setValue(User.strength.get(i));
                    }
                }

                if (User.steps.isEmpty() != true) {
                    PedometerClass tempStep;
                    for (int i = 0; i < User.steps.size(); i++) {
                        ref.child(id).child("Steps").child(String.valueOf(i)).setValue(User.steps.get(i));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
