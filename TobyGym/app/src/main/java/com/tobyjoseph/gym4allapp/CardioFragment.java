package com.tobyjoseph.gym4allapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CardioFragment extends Fragment implements  View.OnClickListener{

    View myView;
    EditText edtTxtCalories, edtTimeTaken, edtDistance;
    Spinner cbxActivity;
    Button btnGetCalories, btnSaveCardio;
    CardioClass cardioActivities;

    Boolean fabOn = true;

    ScrollView scrHistory;
    GridLayout llHistory;

    //// Following links were used to measure the calories for a specific activity
    /// https://www.topendsports.com/weight-loss/energy-met.html
    /// http://keisan.casio.com/exec/system/1350959101
    /// https://www.healthline.com/health/fitness-exercise/running-burn-calories-per-mile#per-mile


    public CardioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_cardio, container, false);
        getActivity().setTitle("Cardio");

        edtTxtCalories = (EditText) myView.findViewById(R.id.edtCalBurned);
        edtDistance = (EditText) myView.findViewById(R.id.edtDistance);
        edtTimeTaken = (EditText) myView.findViewById(R.id.edtTimeTaken);
        cbxActivity = (Spinner) myView.findViewById(R.id.cbxActivities);
        btnGetCalories = (Button) myView.findViewById(R.id.btnGetCalories);

        btnSaveCardio = (Button) myView.findViewById(R.id.btnSaveCardio);
        scrHistory = (ScrollView) myView.findViewById(R.id.scrVwHistory);
        llHistory = (GridLayout) myView.findViewById(R.id.LLHistory);


        btnGetCalories.setOnClickListener(this);
        btnSaveCardio.setOnClickListener(this);
        myView.findViewById(R.id.fabCardioHistory).setOnClickListener(this);

        return myView;
    }


    @Override
    public void onClick(View v) {

        if (R.id.fabCardioHistory == v.getId()){

            ViewCompat.animate(myView.findViewById(R.id.fabCardioHistory)).rotation(135f).
                    withLayer().
                    setDuration(400).
                    setInterpolator(new OvershootInterpolator(10.0F)).
                    start();

            if(fabOn == true) {
                scrHistory.setVisibility(View.VISIBLE);
                btnSaveCardio.setVisibility(View.GONE);
                EditText userCardioTit;
                userCardioTit = new EditText(myView.getContext());
                userCardioTit.setText("Activity Type                   Date         Calories");
                userCardioTit.setTypeface(Typeface.DEFAULT_BOLD);
                userCardioTit.setTextSize(20);
                llHistory.addView(userCardioTit);
                TextView userCardio;
                for (int i = 0; i < User.cardio.size(); i++) {
                    userCardio = new TextView(myView.getContext());
                    userCardio.setText("        "+User.cardio.get(i).ActivityType);
                    userCardio.setTypeface(Typeface.DEFAULT_BOLD);
                    userCardio.setTextSize(16);
                    llHistory.addView(userCardio);

                    userCardioTit = new EditText(myView.getContext());
                    userCardioTit.setText("                                                   "+User.cardio.get(i).Date+"           "+User.cardio.get(i).CaloriesBurned+"       ");
                    userCardioTit.setTypeface(Typeface.DEFAULT_BOLD);
                    userCardioTit.setTextSize(16);
                    llHistory.addView(userCardioTit);
                }
                fabOn = false;
            }else{
                ViewCompat.animate(myView.findViewById(R.id.fabCardioHistory)).rotation(0f).
                        withLayer().
                        setDuration(400).
                        setInterpolator(new OvershootInterpolator(10.0F)).
                        start();
                scrHistory.setVisibility(View.GONE);
                btnSaveCardio.setVisibility(View.VISIBLE);
                llHistory.removeAllViews();

                fabOn = true;
            }


        }else if (R.id.btnSaveCardio == v.getId()){
            if (!(cbxActivity.getSelectedItem().toString().equals("Please Choose an Activity")&&!(edtTxtCalories.getText().toString().equals("")))){
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String date1 = dateFormat.format(date).toString();

                cardioActivities = new CardioClass(cbxActivity.getSelectedItem().toString(),Integer.valueOf(edtTxtCalories.getText().toString()),date1);
                User.cardio.add(cardioActivities);
                ///// save calories and activity to database
                Toast.makeText(myView.getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(myView.getContext(), "Please choose an activity", Toast.LENGTH_SHORT).show();
            }

        }else if(R.id.btnGetCalories == v.getId()){
            double calories = 0;
            DecimalFormat format = new DecimalFormat("##");

            int bmr = User.calculateBMR();
            double met = 0.0;
            switch (cbxActivity.getSelectedItem().toString())
            {
                case "Jogging":
                    met = 8;
                    break;
                case "Running":
                    met = 11.5;
                    break;
                case "Cycling Fast":
                    met = 8;
                    break;
                case "Cycling Slow":
                    met = 4;
                    break;
                case "Swimming Fast":
                    met = 7;
                    break;
                case "Swimming Slow":
                    met = 4.5;
                    break;
                default:
                    Toast.makeText(myView.getContext(), "Please Select an Activity", Toast.LENGTH_SHORT).show();
                    break;
            }
            calories = bmr * (met/24.0) * (Integer.valueOf(edtTimeTaken.getText().toString())/60.0);
            String calString = String.valueOf(Math.round(Float.parseFloat(format.format(calories))));
            edtTxtCalories.setText(""+calString.toString());
        }

    }
}
