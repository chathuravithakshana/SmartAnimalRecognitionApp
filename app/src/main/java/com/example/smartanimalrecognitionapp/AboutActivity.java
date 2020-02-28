package com.example.smartanimalrecognitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.net.URLEncoder;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //load about details with webview
        WebView view = (WebView) findViewById(R.id.webViewResult);
        view.setVerticalScrollBarEnabled(false);
        view.loadData("<html><body style = 'text-align:justify'><p>Smart Animal Recognition is an Android mobile application based on a designed IoT module which is used to locate and identify animals by their voice remotely. This research project has been done as a requirement for partial fulfillment of my Honours degree programme in Computer Studies. <br><br> The audio and location data which is sent by Arduino based IoT module (NodeMCU with mic) was streamed over Firebase Realtime database to this mobile application and identify animal's voice by using the pre-trained CNN (Convolutional Neural Network) model (Chosen optimizer was the AdaDelta optimizer as the best one among observed optimizers). Also location of the call is displayed using the Google Map API.</p></body></html>", "text/html", "utf-8");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //go to main again
        Intent intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
