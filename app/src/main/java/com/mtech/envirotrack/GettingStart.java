package com.mtech.envirotrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class GettingStart extends AppCompatActivity {

    Button btnGetStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_start);

        btnGetStarted = findViewById(R.id.getStartedButton);
        changeStatusBarColor(getResources().getColor(R.color.dark_green));


        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GettingStart.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        window.getDecorView().setSystemUiVisibility(0);

    }
}