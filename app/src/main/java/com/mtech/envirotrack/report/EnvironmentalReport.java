package com.mtech.envirotrack.report;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.mtech.envirotrack.R;

import java.util.Calendar;

public class EnvironmentalReport extends AppCompatActivity {

    private Spinner environmentalIncidentTypeSpinner;

    private EditText excessEmissionDatePicker;
    private EditText excessEmissionTimePicker;
    private EditText incidentReportDatePicker;
    private EditText incidentReportTimePicker;

    private LinearLayout excessEmissionLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmental_report);

        excessEmissionDatePicker = findViewById(R.id.ExcessEmissionDatePicker);
        excessEmissionTimePicker = findViewById(R.id.ExcessEmissionTimePicker);
        incidentReportDatePicker = findViewById(R.id.incidenReportDatePicker);
        incidentReportTimePicker = findViewById(R.id.incidentReportTimePicker);

        excessEmissionLayout = findViewById(R.id.excessEmissionDetails);

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

        environmentalIncidentTypeSpinner = findViewById(R.id.environmentalIncidentTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.enviromentalIncidentType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        environmentalIncidentTypeSpinner.setAdapter(adapter);
        environmentalIncidentTypeSpinner.setSelection(0, false); // This will prevent automatic selection
        environmentalIncidentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Dummy entry
                        // Do nothing
                        break;
                    case 1: // Excess Emission
                        excessEmissionLayout.setVisibility(View.VISIBLE);
                        break;
                    case 2: // Spill
                        // Make the Spill layout visible
                        break;
                    case 3: // Other
                        // Make the Other layout visible
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                excessEmissionDatePicker.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                excessEmissionTimePicker.setText(hourOfDay + ":" + minute);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }
}