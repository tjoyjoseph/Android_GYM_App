package com.tobyjoseph.gym4allapp;



import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//// References
/// https://en.wikipedia.org/wiki/One-repetition_maximum#Epley_formula to get the one rep max formulae



public class StrengthFragment extends Fragment implements View.OnClickListener{

    View myView;
    Button btnSave, btnDecWeight, btnDecRep, btnDecSet, btnIncWeight, btnIncRep, btnIncSet;
    EditText edtWeight, edtRep, edtSet;
    Spinner cbxStrengthActivities;

    ScrollView scrHistory;
    LinearLayout llHistory;
    Boolean fabOn = true;

    public StrengthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_strength, container, false);
        getActivity().setTitle("Strength");

        // Inflate the layout for this fragment

        btnSave = (Button) myView.findViewById(R.id.btnSaveWorkout);
        btnDecRep = (Button) myView.findViewById(R.id.btnDecRep);
        btnDecSet = (Button) myView.findViewById(R.id.btnDecSet);
        btnDecWeight = (Button) myView.findViewById(R.id.btnDecWeight);
        btnIncRep = (Button) myView.findViewById(R.id.btnIncRep);
        btnIncSet = (Button) myView.findViewById(R.id.btnIncSet);
        btnIncWeight = (Button) myView.findViewById(R.id.btnIncWeight);

        scrHistory = (ScrollView) myView.findViewById(R.id.scrVwHistoryStrength);
        llHistory = (LinearLayout) myView.findViewById(R.id.LLHistoryStrength);

        btnSave.setOnClickListener(this);
        btnDecRep.setOnClickListener(this);
        btnDecSet.setOnClickListener(this);
        btnDecWeight.setOnClickListener(this);
        btnIncRep.setOnClickListener(this);
        btnIncSet.setOnClickListener(this);
        btnIncWeight.setOnClickListener(this);
        myView.findViewById(R.id.fabStrengthHistory).setOnClickListener(this);
        myView.findViewById(R.id.btnOneRepMax).setOnClickListener(this);

        edtWeight = (EditText) myView.findViewById(R.id.edtWeight);
        edtRep = (EditText) myView.findViewById(R.id.edtRep);
        edtSet = (EditText) myView.findViewById(R.id.edtSet);

        cbxStrengthActivities = (Spinner) myView.findViewById(R.id.cbxStrengthActivities);


        return myView;
    }


    @Override
    public void onClick(View v) {
        int number = 0;
        switch(v.getId()) {
            case R.id.btnOneRepMax:
                int weight = Integer.valueOf(edtWeight.getText().toString());
                int rep = Integer.valueOf(edtRep.getText().toString());
                DecimalFormat format = new DecimalFormat("##.#");
                String oneRepMax = format.format(weight*(36.0/(37.0-rep)));
                Toast.makeText(myView.getContext(), ""+oneRepMax, Toast.LENGTH_SHORT).show();

                break;
            case R.id.fabStrengthHistory :
                ViewCompat.animate(myView.findViewById(R.id.fabStrengthHistory)).rotation(135f).
                        withLayer().
                        setDuration(400).
                        setInterpolator(new OvershootInterpolator(10.0F)).
                        start();

                if(fabOn == true) {
                    scrHistory.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.GONE);
                    EditText userStrengthTit;
                    userStrengthTit = new EditText(myView.getContext());
                    userStrengthTit.setText("Activity Type                   Date         Volume");
                    userStrengthTit.setTypeface(Typeface.DEFAULT_BOLD);
                    userStrengthTit.setTextSize(20);
                    llHistory.addView(userStrengthTit);
                    TextView userStrength;
                    for (int i = 0; i < User.strength.size(); i++) {
                        userStrength = new TextView(myView.getContext());
                        userStrength.setText(""+User.strength.get(i).activityType);
                        userStrength.setTypeface(Typeface.DEFAULT_BOLD);
                        userStrength.setTextSize(16);
                        llHistory.addView(userStrength);

                        userStrengthTit = new EditText(myView.getContext());
                        userStrengthTit.setText("                                                   "+User.strength.get(i).date+"           "+User.strength.get(i).volume+"       ");
                        userStrengthTit.setTypeface(Typeface.DEFAULT_BOLD);
                        userStrengthTit.setTextSize(16);
                        llHistory.addView(userStrengthTit);
                    }
                    fabOn = false;
                }else{
                    ViewCompat.animate(myView.findViewById(R.id.fabStrengthHistory)).rotation(0f).
                            withLayer().
                            setDuration(400).
                            setInterpolator(new OvershootInterpolator(10.0F)).
                            start();
                    scrHistory.setVisibility(View.GONE);
                    btnSave.setVisibility(View.VISIBLE);
                    llHistory.removeAllViews();

                    fabOn = true;
                }
                break;

            case R.id.btnIncRep:
                number  = Integer.valueOf(edtRep.getText().toString())+1;
                edtRep.setText(""+number);
                break;
            case R.id.btnIncSet:
                number  = Integer.valueOf(edtSet.getText().toString())+1;
                edtSet.setText(""+number);
                break;
            case R.id.btnIncWeight:
                number  = Integer.valueOf(edtWeight.getText().toString())+5;
                edtWeight.setText(""+number);
                break;
            case R.id.btnDecRep:
                number  = Integer.valueOf(edtRep.getText().toString())-1;
                if (!(number < 0))
                    edtRep.setText(""+number);
                break;
            case R.id.btnDecSet:
                number  = Integer.valueOf(edtSet.getText().toString())-1;
                if (!(number < 0))
                    edtSet.setText(""+number);
                break;
            case R.id.btnDecWeight:
                number  = Integer.valueOf(edtWeight.getText().toString())-5;
                if (!(number < 0))
                    edtWeight.setText(""+number);
                break;

            case R.id.btnSaveWorkout:
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String date1 = dateFormat.format(date).toString();
                if (!(cbxStrengthActivities.getSelectedItem().equals("Please Choose an Activity"))) {
                    StrengthClass strengthActivity = new StrengthClass(cbxStrengthActivities.getSelectedItem().toString(),Integer.valueOf(edtWeight.getText().toString())*Integer.valueOf(edtWeight.getText().toString())*Integer.valueOf(edtSet.getText().toString()),date1);
                    User.strength.add(strengthActivity);
                    //User.saveData();

                    Toast.makeText(myView.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(myView.getContext(), "Please choose an activity", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }

    }
}
