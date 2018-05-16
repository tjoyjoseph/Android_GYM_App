package com.tobyjoseph.gym4allapp;

import android.Manifest;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static android.support.v4.media.session.MediaButtonReceiver.handleIntent;

// References
///// https://www.youtube.com/watch?v=OxXE7bScRZs NFC Tutorial


public class ScanFragment extends Fragment {

    View myView;
    TextView lblScanPlease;
    Button btnOpenNFC;
    NfcAdapter nfcAdpt;

    PendingIntent nfcPendingIntent;
    IntentFilter[] intentFiltersArray;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((nfcAdpt != null)&&(nfcAdpt.isEnabled())){
            //Toast.makeText(getContext(),"NFC Available",Toast.LENGTH_SHORT).show();
            lblScanPlease.setVisibility(View.VISIBLE);
            btnOpenNFC.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_scan, container, false);
        getActivity().setTitle("Enable Scan");

        lblScanPlease = (TextView) myView.findViewById(R.id.lblPleaseScan);
        btnOpenNFC = (Button) myView.findViewById(R.id.btnNFCon);

        btnOpenNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        nfcAdpt = NfcAdapter.getDefaultAdapter(getContext());

        if ((nfcAdpt != null)&&(nfcAdpt.isEnabled())){
            Toast.makeText(getContext(),"NFC Available",Toast.LENGTH_SHORT).show();
            lblScanPlease.setVisibility(View.VISIBLE);
            btnOpenNFC.setVisibility(View.GONE);
        }else{
            Toast.makeText(getContext(),"Please activate NFC",Toast.LENGTH_SHORT).show();

            btnOpenNFC.setVisibility(View.VISIBLE);
            lblScanPlease.setVisibility(View.GONE);
        }



        return myView;
    }





}
