package com.tobyjoseph.gym4allapp;

import android.Manifest;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;

//// in order to play a youtube video - https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
/// in order to scan OR code - https://www.youtube.com/watch?v=o69UqAKi47I


public class TutorialFragment extends Fragment{

    View myView;

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Button btnScan;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    final int RequestCameraPermission = 1001;


    public TutorialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermission: {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        getActivity().setTitle("Tutorials");





        btnScan = (Button) myView.findViewById(R.id.btnScan);
        cameraPreview = (SurfaceView) myView.findViewById(R.id.cameraPreview);
        txtResult = (TextView) myView.findViewById(R.id.txtResult);

        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector).setRequestedPreviewSize(640, 480).build();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreview.setVisibility(View.VISIBLE);
                txtResult.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.GONE);

            }
        });



        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA},RequestCameraPermission);
                        return;
                    }

                    cameraSource.start(cameraPreview.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0){
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getContext().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            vibrator.cancel();
                            //txtResult.setText(qrcodes.valueAt(0).displayValue);
                            btnScan.setVisibility(View.VISIBLE);
                            txtResult.setVisibility(View.GONE);
                            String youtubeLink = "http://www.youtube.com/watch?v="+qrcodes.valueAt(0).displayValue;

                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)));
                            } catch (ActivityNotFoundException ex) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(youtubeLink)));
                            }

                            cameraSource.stop();
                            cameraPreview.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        return myView;
    }


}
