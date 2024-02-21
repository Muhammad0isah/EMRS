package com.mtech.envirotrack.report;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.user.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Report extends Fragment {


    private View rootView;
    private Button reportButton;

    private PieChart pieChart;


    public Report() {
        // Required empty public constructor
    }

    public static Report newInstance() {
        return new Report();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report, container, false);

        reportButton = rootView.findViewById(R.id.btn_create_report);

        requireActivity().findViewById(R.id.btn_add_report).setVisibility(View.GONE);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()){
                    startActivity(new Intent(getActivity(), EnvironmentalReport.class));
                }
                else{
                    startActivity(new Intent(getActivity(), Login.class));
                }
            }
        });

        pieChart = rootView.findViewById(R.id.pieChart);
        if(isLoggedIn()){
            setupPieChart();
        }
        return rootView;
    }
    private void setupPieChart() {
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        // Retrieve the data from Firebase
        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, Integer> impactTypeCount = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserReport report = snapshot.getValue(UserReport.class);
                    if (report != null) {
                        // Increment the count for the impact type
                        String impactType = report.getImpactType();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            impactTypeCount.put(impactType, impactTypeCount.getOrDefault(impactType, 0) + 1);
                        }
                    }
                }

                // Create PieEntry objects for each impact type
                List<PieEntry> entries = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : impactTypeCount.entrySet()) {
                    entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                }

                PieDataSet set = new PieDataSet(entries, "Environmental Report Results");
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(set);

                pieChart.setData(data);
                pieChart.invalidate(); // refresh

//                Legend legend = pieChart.getLegend();
//                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}