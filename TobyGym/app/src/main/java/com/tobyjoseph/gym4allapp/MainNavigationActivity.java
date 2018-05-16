package com.tobyjoseph.gym4allapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/// mixture of a tutorial and a seperate site was used to get the NFC scanner working
//// https://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
/// https://www.youtube.com/watch?v=bbeS7FPjRNk

public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.app.FragmentManager fragmentManager;
    NfcAdapter nfcAdapter;
    Boolean loggedout = false;
    //TextView lblScanPlease;
    Map<Integer, String> gymLocations = new HashMap<>();
    boolean loopEnd = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Toby","onCreate MainNavigation");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
//        String id = getIntent().getStringExtra("LOGIN_ID");
//        User.email = id;
        DatabaseHandling.retrieveFromDatabase();
        FileHandling.retrieveData(getApplicationContext());


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        gymLocations.put(1853,"Bradford");
        gymLocations.put(4986,"Basildon");
        gymLocations.put(3308,"Nottingham");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //// FLOATING ACTION BAR
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, new GymRoutineFragment()).commit();


    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Toby","onCreate MainNavigation");
//       mEmailIDRef.addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(DataSnapshot dataSnapshot) {
//               if (dataSnapshot.getValue(String.class).equals(User.email))
//               {
//                   User.BFat = Float.parseFloat(mUserRef.child("bodyfat").toString());
//               }
//           }
//
//           @Override
//           public void onCancelled(DatabaseError databaseError) {
//
//           }
//       });
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            //findViewById(R.id.pbrProgress).setVisibility(View.VISIBLE);
            Toast.makeText(this,"Scan successful! Please wait!",Toast.LENGTH_SHORT).show();

            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0){
                try {
                    readTextFromTag((NdefMessage)parcelables[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this,"Scan unsuccessful. Cannot read data.",Toast.LENGTH_SHORT).show();
            }

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        wait(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    findViewById(R.id.pbrProgress).setVisibility(View.GONE);
//                }
//            }).start();


        }
    }

    @Override
    protected void onResume() {
        Log.d("Toby","onResume MainNavigation");
        //User.deleteDetails();
        //DatabaseHandling.retrieveFromDatabase();
        setupForegroundDispatch(this, nfcAdapter);
        super.onResume();
        FileHandling.retrieveData(getApplicationContext());
    }

    @Override
    protected void onRestart() {
        Log.d("Toby","onRestart MainNavigation");
        super.onRestart();
        //DatabaseHandling.retrieveFromDatabase(getApplicationContext());
    }

    @Override
    protected void onPause() {
        Log.d("Toby","onPause MainNavigation");
        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
        if(loggedout != true){
            FileHandling.saveData(getApplicationContext(),false);}



    }

    @Override
    protected void onStop() {
        Log.d("Toby","onStop MainNavigation");
        super.onStop();


            if (loggedout != true) {
                FileHandling.saveData(getApplicationContext(), false);
            }
            DatabaseHandling.saveToFirebase();



    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private void readTextFromTag(NdefMessage ndefMessage) throws InterruptedException {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length >0){
            NdefRecord ndefRecord = ndefRecords[0];
            int tabContent = Integer.valueOf(getTextFromNDEFRecord(ndefRecord));

            Toast.makeText(this,"Success! You've signed into GYM4ALL "+gymLocations.get(tabContent),Toast.LENGTH_LONG).show();


            //lblScanPlease.setText("SUCCESS!!");

        }else{
            Toast.makeText(this,"NO NDEF RECORD!",Toast.LENGTH_SHORT).show();
        }
    }

    public String getTextFromNDEFRecord(NdefRecord ndefRecord){
        String tagContent = null;
        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128)==0)?"UTF-8":"UTF-16";
            int langageSize = payload[0] & 0063;
            tagContent = new String (payload, langageSize +1, payload.length - langageSize -1, textEncoding);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  tagContent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_logOut)
        {
            Log.d("Toby","LogOut MainNavigation");
            Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPrefAdd = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPrefAdd.edit();
            editor.remove("LoginID");
            editor.commit();
            DatabaseHandling.saveToFirebase();
            loggedout = true;
            //DatabaseHandling.saveToFirebase();
            FileHandling.saveData(getApplicationContext(),true);



            finish();

            return true;
        }
        else if (User.heightCM == 0){
//            if (DatabaseHandling.retrieveFromDatabase(getApplicationContext())) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
                Toast.makeText(getApplicationContext(), "Please Fill out the User Details First", Toast.LENGTH_SHORT).show();
//                return false;
//            }else {
//                return true;
//            }
        }else {

            fragmentManager = getFragmentManager();
            if (id == R.id.nav_home) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new GymRoutineFragment()).commit();

            } else if (id == R.id.nav_strength) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new StrengthFragment()).commit();

            } else if (id == R.id.nav_cardio) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CardioFragment()).commit();

            } else if (id == R.id.nav_tutorial) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new TutorialFragment()).commit();

            } else if (id == R.id.nav_steps) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new PedometerFragment()).commit();
            } else if (id == R.id.nav_scan) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ScanFragment()).commit();
            } else if (id == R.id.nav_gymfinder) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new GymFinderFragment()).commit();
            }

//             else if (id == R.id.nav_logOut) {
//                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
//                SharedPreferences sharedPrefAdd = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                SharedPreferences.Editor editor = sharedPrefAdd.edit();
//                editor.remove("LoginID");
//                editor.commit();
//                finish();
//            }


        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    }

