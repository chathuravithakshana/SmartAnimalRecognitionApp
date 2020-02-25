package com.example.smartanimalrecognitionapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;

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


    /*//Load the ML model
    FirebaseCustomRemoteModel remoteModel =
            new FirebaseCustomRemoteModel.Builder("train_6_model").build();

    FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build();
    FirebaseModelManager.getInstance().download(remoteModel, conditions)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            // Success.
        }
    });

    FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
            .setAssetFilePath("model.tflite")
            .build();*/
}
