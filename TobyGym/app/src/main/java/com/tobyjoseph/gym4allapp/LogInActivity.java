package com.tobyjoseph.gym4allapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    EditText edtEmail, edtPassword;

    //ProgressBar pgrLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Toby","onCreate Login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("Login");

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnCreateUser).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);

        SharedPreferences sharedPrefCheck = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);

        String existingLoginID = sharedPrefCheck.getString("LoginID", "NOID");

        if (!existingLoginID.equals("NOID"))
        {
            User.email = existingLoginID;
            User.deleteDetails();
            Toast.makeText(getApplicationContext(), "Already Logged In", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LogInActivity.this,MainNavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("LOGIN_ID", existingLoginID);
            startActivity(intent);
        }
        //User.deleteDetails();

       // pgrLogin.findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreateUser:
                registerUser();
                break;

            case R.id.btnLogin:
                userLogin();
                break;
        }

    }

    private void registerUser() {

        String[] userLoginDetails = validateEmailAndPassword();

        if (userLoginDetails == null){
            return;
        }

        //pgrLogin.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userLoginDetails[0],userLoginDetails[1]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //pgrLogin.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User Registered Successful", Toast.LENGTH_SHORT).show();

                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Useracount already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Try again later", Toast.LENGTH_SHORT).show();
                        System.out.print(task.getException());
                    }
                }
            }


        });
    }

    private void userLogin() {

        final String[] userLoginDetails = validateEmailAndPassword();
        final SharedPreferences sharedPrefAdd = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (userLoginDetails == null){
            return;
        }

        mAuth.signInWithEmailAndPassword(userLoginDetails[0],userLoginDetails[1]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    SharedPreferences.Editor editor = sharedPrefAdd.edit();
                    editor.putString("LoginID", userLoginDetails[0]);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    edtEmail.setText("");
                    edtPassword.setText("");
                    User.email = userLoginDetails[0];
                    User.deleteDetails();

                    Intent intent = new Intent(LogInActivity.this,MainNavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("LOGIN_ID", userLoginDetails[0]);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private String[] validateEmailAndPassword() {

        String[] userLoginDetails = {"",""};

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return null;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please enter a valid email");
            edtEmail.requestFocus();
            return null;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return null;
        }

        if (password.length() < 6) {
            edtPassword.setError("Minimum charater is 6");
            edtPassword.requestFocus();
            return null;
        }

        userLoginDetails[0] = email;
        userLoginDetails[1] = password;

        return  userLoginDetails;
    }
}
