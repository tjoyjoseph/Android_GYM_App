package com.tobyjoseph.gym4allapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

//// https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3445648/ is used to get the calculation for FFMI calculation
///// https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3445648/table/T0001/


public class SettingsFragment extends Fragment implements  View.OnClickListener{
    View myView;
    EditText edtBFat,edtHeight,edtWeight;
    TextView edtDOB;
    Button btnFMI, btnBMR, btnFatFreeMass;
    RadioButton rbMale, rbFemale;
    static  final String DIALOG_ID = "DatePickerTag";
    DatePickerDialog.OnDateSetListener onDateSetListener;

    //int year = 8,month = 4,day = 1996;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_settings, container, false);



        getActivity().setTitle("Settings");


        edtBFat = (EditText) myView.findViewById(R.id.edtBodyFat);
        edtHeight = (EditText) myView.findViewById(R.id.edtHeight);
        edtWeight = (EditText) myView.findViewById(R.id.edtWeight);
        edtDOB = (TextView) myView.findViewById(R.id.edtDOB);
        btnFMI = (Button) myView.findViewById(R.id.btnFFMI);
        btnBMR = (Button) myView.findViewById(R.id.btnBMR);
        btnFatFreeMass = (Button) myView.findViewById(R.id.btnFatFreeMass);
        rbFemale = (RadioButton) myView.findViewById(R.id.rbFemale);
        rbMale = (RadioButton)  myView.findViewById(R.id.rbMale);

        if (!(User.heightCM == 0)){
            edtHeight.setText(Float.toString(User.heightCM));
        }
        if (!(User.weightKG == 0)){
            edtWeight.setText(Float.toString(User.weightKG));
        }
        if (!(User.BFat == 0)){
            edtBFat.setText(Float.toString(User.BFat));
        }
        if (!(User.year == 0)){
            edtDOB.setText(User.day+"/"+ User.month+"/"+ User.day);
        }
        if (User.sex == 'M'){
            rbMale.setChecked(true);
        }else{
            rbFemale.setChecked(true);
        }


        btnFMI.setOnClickListener(this);
        btnFatFreeMass.setOnClickListener(this);
        btnBMR.setOnClickListener(this);
        edtDOB.setOnClickListener(this);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int iyear, int imonth, int idayOfMonth) {
                User.year = iyear;
                User.month = imonth+1;
                User.day = idayOfMonth;
                String date = User.day + "/" + User.month + "/" + User.year;
                edtDOB.setText(date);

            }
        };

        //rbMale.setChecked(true);

        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    rbFemale.setChecked(false);
                    User.sex = 'M';
                }
                //rbMale.setChecked(true);
            }
        });
        rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    rbMale.setChecked(false);
                    User.sex = 'F';
                }
                //rbFemale.setChecked(true);
            }
        });

        edtBFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(s.toString().equals(""))) {
                    User.BFat = new Float(s.toString());
                }
            }
        });

        edtHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(s.toString().equals(""))) {
                    User.heightCM = new Float(s.toString());
                }
            }
        });

        edtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(s.toString().equals(""))) {
                    User.weightKG = new Float(s.toString());
                }
            }
        });

//        btnFMI.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!(edtBFat.getText().toString().equals("")) && !(edtheight.getText().toString().equals("")) && !(edtWeight.getText().toString().equals(""))) {
//                    ////TODO generate FFMI here
//
//                    Float LeanWeight = User.weightKG * (1 - (User.BFat / 100));
//                    Double FFMI = (LeanWeight) * (User.heightCM/100) / 5.83194 ;
//                    FFMI = Double.parseDouble(String.format("%.2f", FFMI));
//                    Double adjustedFFMI = FFMI + 6.3 * (1.8 - (User.heightCM/100) );
//                    adjustedFFMI = Double.parseDouble(String.format("%.2f", adjustedFFMI));
//                    Toast.makeText(myView.getContext(), "FFMI is "+FFMI.toString()+" and Adjusted FFMI is "+adjustedFFMI, Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(myView.getContext(), "Please Enter Body Fat, Height, and Weight", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        return myView;
    }




    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btnBMR:
                if (!(User.heightCM == 0) && !(User.year == 0) && !(User.weightKG == 0)) {
                    int BMR = User.calculateBMR();
                    Toast.makeText(myView.getContext(), "Basal Metabolic Rate is "+Double.toString(BMR), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(myView.getContext(), "Please Enter DOB, Height, and Weight", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnFFMI:
                if (!(User.BFat == 0) && !(User.heightCM == 0) && !(User.weightKG == 0)) {
                    ////TODO generate FFMI here

                    Float LeanWeight = User.weightKG * (1 - (User.BFat / 100));
                    Float FFMI = LeanWeight / ((User.heightCM/100) * (User.heightCM/100));
                    FFMI = Float.parseFloat(String.format("%.2f", FFMI));
                    //Double adjustedFFMI = FFMI + 6.3 * (1.8 - (User.heightCM/100) );
                    //adjustedFFMI = Double.parseDouble(String.format("%.2f", adjustedFFMI));
                    Toast.makeText(myView.getContext(), "Fat Free Mas Index is "+FFMI.toString(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(myView.getContext(), "Please Enter Body Fat, Height, and Weight", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnFatFreeMass:

                if (!(User.BFat == 0) && !(User.weightKG == 0)) {
                    Float LeanWeight = User.weightKG * (1 - (User.BFat / 100));

                    LeanWeight = Float.parseFloat(String.format("%.2f", LeanWeight));

                    Toast.makeText(myView.getContext(), "Lean Body Mass is "+LeanWeight.toString(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(myView.getContext(), "Please Enter Body Fat and Weight", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edtDOB:
                Calendar cal = Calendar.getInstance();
                int myear = cal.get(Calendar.YEAR);
                int mmonth = cal.get(Calendar.MONTH);
                int mday = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(myView.getContext(), R.style.AppTheme_PopupOverlay,onDateSetListener,1996,7,3);

                dialog.show();
                break;

        }

    }
}
