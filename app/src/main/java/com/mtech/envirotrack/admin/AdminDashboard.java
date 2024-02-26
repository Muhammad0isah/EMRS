package com.mtech.envirotrack.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.report.Report;
import com.mtech.envirotrack.report.ReportAdapter;
import com.mtech.envirotrack.report.UserReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends AppCompatActivity {

    private ImageButton goBackButton, logoutButton;
    private FirebaseAuth mAuth;
    int serialNumber = 1; // Initialize serial number


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logoutButton);
        goBackButton = findViewById(R.id.goBackButton);


        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new ViewReports());
        viewPagerAdapter.addFragment(new SendNotification());
        viewPagerAdapter.addFragment(new CreateAdmin());

        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("View Report");
                            break;
                        case 1:
                            tab.setText("Send Notification");
                            break;
                        case 2:
                            tab.setText("Add Admin");
                            break;
                    }
                }).attach();

        // Retrieve data from the Firebase Realtime Database
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<User> reports = new ArrayList<>();
//                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
//
//                    // Retrieve reports for the user
//                    DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("users").child(userSnapshot.getKey()).child("reports");
//                    reportsRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot reportSnapshot: dataSnapshot.getChildren()) {
//                                String reportNumber = reportSnapshot.child("reportNumber").getValue(String.class);
//                                String userEmail = reportSnapshot.child("userEmail").getValue(String.class);
//                                String userName = reportSnapshot.child("userName").getValue(String.class);
//                                String impactType = reportSnapshot.child("impactType").getValue(String.class);
//                                Map<String, String> data = reportSnapshot.child("data").getValue(new GenericTypeIndicator<Map<String, String>>() {});
//                                String attachment = reportSnapshot.child("attachment").getValue(String.class); // Retrieve attachment
//
//                                if (data != null && !data.isEmpty()) {
//                                    User user = new User(reportNumber, userEmail, userName, impactType, data, attachment);
//                                    user.setSerialNumber(serialNumber++); // Set and increment serial number only if data exists
//                                    reports.add(user);
//                                }
//                            }
//
//                            // Set the adapter for the RecyclerView
//                            RecyclerView recyclerView = findViewById(R.id.recycler_view);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(AdminDashboard.this));
//                            UserReportAdapter adapter = new UserReportAdapter(reports);
//                            recyclerView.setAdapter(adapter);
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle error
//                            Toast.makeText(AdminDashboard.this, "Failed to load data", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error
//                Toast.makeText(AdminDashboard.this, "Failed to load data", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}