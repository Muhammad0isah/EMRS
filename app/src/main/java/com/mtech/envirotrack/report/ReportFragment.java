package com.mtech.envirotrack.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.mtech.envirotrack.R;

public class ReportFragment extends Fragment {

    private LinearLayout environmentalIncidentReportLayout;
    private LinearLayout waterQualityReportLayout;
    private LinearLayout wasteManagementReportLayout;
    private LinearLayout pollutionSourceReportLayout;
    private LinearLayout weatherImpactReportLayout;

    private View rootView;
    private Spinner reportTypeSpinner;

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

        environmentalIncidentReportLayout = rootView.findViewById(R.id.environmentalIncidentReportLayout);
        waterQualityReportLayout = rootView.findViewById(R.id.waterQualityReportLayout);
        wasteManagementReportLayout = rootView.findViewById(R.id.wasteManagementReportLayout);
        pollutionSourceReportLayout = rootView.findViewById(R.id.pollutionSourceReportLayout);
        weatherImpactReportLayout = rootView.findViewById(R.id.weatherImpactReportLayout);

        reportTypeSpinner = rootView.findViewById(R.id.reportTypeSpinner);
        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedReportType = parent.getItemAtPosition(position).toString();
                switch (selectedReportType) {
                    case "Environmental Incident":
                        environmentalIncidentReportLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Water Quality":
                        waterQualityReportLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Waste Management":
                        wasteManagementReportLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Pollution Source":
                        pollutionSourceReportLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Weather Impact":
                        weatherImpactReportLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return rootView;
    }
}