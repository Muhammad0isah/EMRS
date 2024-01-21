package com.mtech.envirotrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mtech.envirotrack.user.Login;
import com.mtech.envirotrack.user.Signup;

public class GettingStart extends AppCompatActivity {

    Button btnGetStarted, log_in, reg_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_start);

        btnGetStarted = findViewById(R.id.getStartedButton);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GettingStart.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}