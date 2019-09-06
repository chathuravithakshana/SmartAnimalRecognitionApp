package com.example.smartanimalrecognitionapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private int splashTime = 3000; //3 seconds
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                Intent intent = new Intent(SplashScreenActivity.this, RecordVoiceActivity.class);
                startActivity(intent);
                finish();
            }
        },splashTime);
    }
}
