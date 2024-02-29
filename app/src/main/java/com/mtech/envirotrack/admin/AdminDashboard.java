package com.mtech.envirotrack.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;

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
        // Set the tab names
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


    }
}