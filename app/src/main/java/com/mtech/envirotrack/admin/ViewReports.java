package com.mtech.envirotrack.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.mtech.envirotrack.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewReports extends Fragment {

    private RecyclerView recyclerView;
    private UserReportAdapter adapter;
    private int serialNumber = 1; // Initialize serial number
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_reports, container, false);


        // Retrieve data from the Firebase Realtime Database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> reports = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {

                    // Retrieve reports for the user
                    DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("users").child(userSnapshot.getKey()).child("reports");
                    reportsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot reportSnapshot: dataSnapshot.getChildren()) {
                                String reportNumber = reportSnapshot.child("reportNumber").getValue(String.class);
                                String userEmail = reportSnapshot.child("userEmail").getValue(String.class);
                                String userName = reportSnapshot.child("userName").getValue(String.class);
                                String impactType = reportSnapshot.child("impactType").getValue(String.class);
                                String status = reportSnapshot.child("status").getValue(String.class); // Retrieve status
                                Map<String, String> data = reportSnapshot.child("data").getValue(new GenericTypeIndicator<Map<String, String>>() {});
                                Map<String, String> attachments = (Map<String, String>) reportSnapshot.child("attachments").getValue(); // Retrieve attachments

                                if (data != null && !data.isEmpty()) {
                                    User user = new User(reportNumber, userEmail, userName, impactType, status, data, attachments);
                                    user.setSerialNumber(serialNumber++); // Set and increment serial number only if data exists
                                    reports.add(user);
                                }
                            }
                            recyclerView = view.findViewById(R.id.recycler_view);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            UserReportAdapter adapter = new UserReportAdapter(reports);
                            recyclerView.setAdapter(adapter);

                            // Update the adapter with the new data
//                            adapter.updateData(reports);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(getActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Toast.makeText(getActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}