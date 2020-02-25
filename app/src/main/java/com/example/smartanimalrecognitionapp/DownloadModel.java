package com.example.smartanimalrecognitionapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;

public class DownloadModel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Define remote model
        final FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("model").build();

        //Download remote model
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


        //Configure the local model
        final FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("model.tflite")
                .build();


        //Create interpreter from the model
        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isDownloaded) {
                        FirebaseModelInterpreterOptions options;
                        if (isDownloaded) {
                            options = new FirebaseModelInterpreterOptions.Builder(remoteModel).build();
                        } else {
                            options = new FirebaseModelInterpreterOptions.Builder(localModel).build();
                        }
                        try {
                            FirebaseModelInterpreter interpreter = FirebaseModelInterpreter.getInstance(options);
                        } catch (FirebaseMLException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }




}
