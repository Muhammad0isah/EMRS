package com.mtech.envirotrack.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mtech.envirotrack.R;

import java.util.ArrayList;
import java.util.List;

public class ReportHistory extends Fragment {

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private List<UserReport> reportList;

    public ReportHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report_history, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(getContext(), reportList);
        recyclerView.setAdapter(reportAdapter);

        readReports();

        return rootView;
    }

    private void readReports() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("reports");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserReport report = snapshot.getValue(UserReport.class);
                    reportList.add(report);
                }
                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load reports", Toast.LENGTH_SHORT).show();
            }
        });
    }
}