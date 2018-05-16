package com.tobyjoseph.gym4allapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

///References
/// https://stackoverflow.com/questions/9666030/display-pdf-file-inside-my-android-application PDF VIEWER

public class GymRoutineFragment extends Fragment {
    View myView;
    PDFView pdfView;

    public GymRoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        myView = inflater.inflate(R.layout.fragment_gym_routine, container, false);
        getActivity().setTitle("About Us");

        pdfView = (PDFView) myView.findViewById(R.id.pdfViewer);
        pdfView.setPositionOffset(0);
        //pdfView.zoomTo(pdfView.getMaxZoom());
        pdfView.fromAsset("Gym4All_About_us.pdf").onRender(new OnRenderListener() {
            @Override
            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                pdfView.fitToWidth();
            }
        }).load();

        return myView;
    }
}
