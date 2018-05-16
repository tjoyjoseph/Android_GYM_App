package com.tobyjoseph.gym4allapp;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// References
///// https://www.youtube.com/watch?v=OxXE7bScRZs NFC Tutorial


public class GymFinderFragment extends Fragment implements OnMapReadyCallback {

    View myView;

    MapView mapView;
    GoogleMap map;


    public GymFinderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_gymfinder, container, false);
        getActivity().setTitle("Gym Finder");

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) myView.findViewById(R.id.mapGym);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(this);


        return myView;
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
//        if (ActivityCompat.checkSelfPermission(myView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(myView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(myView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(myView.getContext(),
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        //map.setMyLocationEnabled(true);
//
//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(52.963251, -1.151714)));



        if (map != null) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(52.958566, -1.145934)).title("Nottingham")
                    .icon(BitmapDescriptorFactory.fromAsset("logo_web.PNG"))
                    .draggable(false).visible(true));

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(51.569621, 0.460498)).title("Basildon")
                    .icon(BitmapDescriptorFactory.fromAsset("logo_web.PNG"))
                    .draggable(false).visible(true));

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(53.791700, -1.747752)).title("Bradford")
                    .icon(BitmapDescriptorFactory.fromAsset("logo_web.PNG"))
                    .draggable(false).visible(true));
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.579722, -0.942131), 7));
    }
}
