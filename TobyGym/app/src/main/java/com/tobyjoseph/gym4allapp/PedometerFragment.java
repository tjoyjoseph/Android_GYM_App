package com.tobyjoseph.gym4allapp;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/////// Refrenences
///http://www.lewisgavin.co.uk/Step-Tracker-Android/
// Code from this website was used and modified to implement some features


public class PedometerFragment extends Fragment implements SensorEventListener, View.OnClickListener {

    View myView;

    SensorManager sensorManager;
    TextView  lblSteps,lblLMWalked, lblCalBurned;
    Button btnStart;
    Button  btnStop;
    boolean running = false;
    int stepCounter = 0;
    float previousSteps = 0;
    final double STEPStoKM = 1312.3359580052;
    double kmWalked = 0.0;

    Timer myTimer;
    TimerTask myTimerTask;
    int seconds = 0;

    ScrollView scrHistory;
    LinearLayout llHistory;
    Boolean fabOn = true;

    public PedometerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_pedometer, container, false);
        getActivity().setTitle("Step Counter");

        scrHistory = (ScrollView) myView.findViewById(R.id.scrStepsHistory);
        llHistory = (LinearLayout) myView.findViewById(R.id.llStepsHistory);
        myView.findViewById(R.id.fabStepHistory).setOnClickListener(this);


        lblSteps = (TextView) myView.findViewById(R.id.lblSteps);
        lblLMWalked = (TextView) myView.findViewById(R.id.lblKMWalked);
        lblCalBurned = (TextView) myView.findViewById(R.id.lblWalkCalBurned);
        btnStart = (Button) myView.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnStop = (Button) myView.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        return myView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fabStepHistory :

                ViewCompat.animate(myView.findViewById(R.id.fabStepHistory)).rotation(135f).
                        withLayer().
                        setDuration(400).
                        setInterpolator(new OvershootInterpolator(10.0F)).
                        start();

                if(fabOn == true) {
                    scrHistory.setVisibility(View.VISIBLE);
                    EditText userStepTit;
                    userStepTit = new EditText(myView.getContext());
                    userStepTit.setText("Steps Walked                  Date         Calories");
                    userStepTit.setTypeface(Typeface.DEFAULT_BOLD);
                    userStepTit.setTextSize(20);
                    llHistory.addView(userStepTit);
                    TextView userStep;
                    for (int i = 0; i < User.steps.size(); i++) {
                        userStep = new TextView(myView.getContext());
                        userStep.setText("              "+User.steps.get(i).stepsWalked);
                        userStep.setTypeface(Typeface.DEFAULT_BOLD);
                        userStep.setTextSize(16);
                        llHistory.addView(userStep);

                        userStepTit = new EditText(myView.getContext());
                        userStepTit.setText("                                                   "+User.steps.get(i).date+"           "+User.steps.get(i).caloriesBurned+"       ");
                        userStepTit.setTypeface(Typeface.DEFAULT_BOLD);
                        userStepTit.setTextSize(16);
                        llHistory.addView(userStepTit);
                    }
                    fabOn = false;
                }else{
                    ViewCompat.animate(myView.findViewById(R.id.fabStepHistory)).rotation(0f).
                            withLayer().
                            setDuration(400).
                            setInterpolator(new OvershootInterpolator(10.0F)).
                            start();
                    scrHistory.setVisibility(View.GONE);
                    llHistory.removeAllViews();

                    fabOn = true;
                }
            case R.id.btnStart:
                lblCalBurned.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                lblSteps.setVisibility(View.VISIBLE);
                lblSteps.setText("0");
                lblLMWalked.setVisibility(View.VISIBLE);
                stepCounter = 0;
                running = true;
                Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
                if (countSensor != null)
                {
                    sensorManager.registerListener(this, countSensor, 1);

                    if (myTimer != null)
                        myTimer.cancel();

                    myTimer = new Timer();

                    myTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            seconds = seconds + 1;
                        }
                    },0, 1000);



                }else {
                    Toast.makeText(getActivity(), "Sensor Not Found", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnStop:

                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
                running = false;
                sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));

                myTimer.cancel();
                lblCalBurned.setVisibility(View.VISIBLE);
                DecimalFormat format = new DecimalFormat("##");
                double calories = User.calculateBMR() * (3.5/24.0) * ((seconds/60.0)/60.0);
                String calString = String.valueOf(Math.round(Float.parseFloat(format.format(calories))));
                lblCalBurned.setText("Calories Burned: "+calString.toString());

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String date1 = dateFormat.format(date).toString();
                PedometerClass stepActivity = new PedometerClass(lblSteps.getText().toString(),calString,date1);
                User.steps.add(stepActivity);
                break;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent e) {

        if (running){
            DecimalFormat format = new DecimalFormat("##.###");

            stepCounter = stepCounter + 1; ///change
           // Toast.makeText(getActivity(), String.valueOf(stepCounter), Toast.LENGTH_SHORT).show();

            lblSteps.setText(String.valueOf(stepCounter));


            kmWalked = stepCounter / STEPStoKM;

            if (kmWalked > 0.01) {
                String kmWalkedString = format.format(kmWalked);
                lblLMWalked.setText("KM Walked is: " + kmWalkedString);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
