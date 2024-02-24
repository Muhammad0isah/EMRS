package com.mtech.envirotrack.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.report.UserReport;

import java.util.Map;

public class AdminDashboard extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logoutButton);

        // Initialize the LinearLayout
        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        // Retrieve data from the Firebase Realtime Database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder jsonDataBuilder = new StringBuilder();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {

                    // Retrieve reports for the user
                    DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("users").child(userSnapshot.getKey()).child("reports");
                    reportsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot reportSnapshot: dataSnapshot.getChildren()) {
                                // Get the JSON data of the report
                                String jsonData = reportSnapshot.getValue().toString();
                                jsonDataBuilder.append(jsonData).append("\n");
                            }
                            // Set the JSON data to the TextView
                            TextView jsonDataView = new TextView(AdminDashboard.this);
                            jsonDataView.setText(jsonDataBuilder.toString());
                            linearLayout.addView(jsonDataView);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}