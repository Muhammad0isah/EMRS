package com.mtech.envirotrack.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;

import java.util.Calendar;

public class ReportFragment extends Fragment {

    private LinearLayout environmentalIncidentReportLayout;
    private LinearLayout waterQualityReportLayout;
    private LinearLayout wasteManagementReportLayout;
    private LinearLayout pollutionSourceReportLayout;
    private LinearLayout weatherImpactReportLayout;
    private LinearLayout otherReportLayout;
    private LinearLayout excessEmissionLayout;
    private Spinner environmentalIncidentTypeSpinner;

    private EditText excessEmissionDatePicker;
    private EditText excessEmissionTimePicker;
    private EditText incidentReportDatePicker;
    private EditText incidentReportTimePicker;

    private View rootView;
    private Button reportTypeButton, historyButton, submitButton;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Hide the toolbar and the line view

        ((MainActivity) getActivity()).findViewById(R.id.coordinatorLayout).setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.toolbar).setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.line_view).setVisibility(View.GONE);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report, container, false);

        environmentalIncidentReportLayout = rootView.findViewById(R.id.environmentalIncidentReportLayout);
        waterQualityReportLayout = rootView.findViewById(R.id.waterQualityReportLayout);
        wasteManagementReportLayout = rootView.findViewById(R.id.wasteManagementReportLayout);
        pollutionSourceReportLayout = rootView.findViewById(R.id.pollutionSourceReportLayout);
        weatherImpactReportLayout = rootView.findViewById(R.id.weatherImpactReportLayout);
        otherReportLayout = rootView.findViewById(R.id.otherReportLayout);
        excessEmissionLayout = rootView.findViewById(R.id.excessEmissionDetails);

        excessEmissionDatePicker = rootView.findViewById(R.id.ExcessEmissionDatePicker);
        excessEmissionTimePicker = rootView.findViewById(R.id.ExcessEmissionTimePicker);
        incidentReportDatePicker = rootView.findViewById(R.id.incidenReportDatePicker);
        incidentReportTimePicker = rootView.findViewById(R.id.incidentReportTimePicker);

        excessEmissionDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        excessEmissionTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        incidentReportDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        incidentReportTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        environmentalIncidentTypeSpinner = rootView.findViewById(R.id.environmentalIncidentTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.enviromentalIncidentType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        environmentalIncidentTypeSpinner.setAdapter(adapter);
        environmentalIncidentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                excessEmissionLayout.setVisibility(View.GONE);
                switch (position) {
                    case 0: // Excess Emission
                        excessEmissionLayout.setVisibility(View.VISIBLE);
                        break;
                    case 1: // Spill
                        // Make the Spill layout visible
                        break;
                    case 2: // Other
                        // Make the Other layout visible
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        reportTypeButton = rootView.findViewById(R.id.reportTypeButton);
        historyButton = rootView.findViewById(R.id.reportHistoryeButton);
        reportTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Choose a report type")
                        .setItems(R.array.report_types, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position of the selected item
                                String[] reportTypes = getResources().getStringArray(R.array.report_types);
                                reportTypeButton.setText(reportTypes[which]);

                                environmentalIncidentReportLayout.setVisibility(View.GONE);
                                waterQualityReportLayout.setVisibility(View.GONE);
                                wasteManagementReportLayout.setVisibility(View.GONE);
                                pollutionSourceReportLayout.setVisibility(View.GONE);
                                weatherImpactReportLayout.setVisibility(View.GONE);
                                otherReportLayout.setVisibility(View.GONE);


                                // Show the layout for the selected report type
                                switch (reportTypes[which]) {
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
                                    case "Other":
                                        otherReportLayout.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the report history activity

            }
        });
        return rootView;
    }

    public void showDatePickerDialog(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year, month, dayOfMonth) -> ((EditText) v).setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                (view, hourOfDay, minute) -> ((EditText) v).setText(hourOfDay + ":" + minute),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }
}