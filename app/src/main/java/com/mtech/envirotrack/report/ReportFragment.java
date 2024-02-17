package com.mtech.envirotrack.report;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;


public class ReportFragment extends Fragment {


    private View rootView;
    private Button reportButton;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report, container, false);

        reportButton = rootView.findViewById(R.id.btn_create_report);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EnvironmentalReport.class));
            }
        });

        return rootView;
    }

}