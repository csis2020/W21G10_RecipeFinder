package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    ImageView imageViewbg,imageView5,imageView6;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageViewbg = findViewById(R.id.imageViewbg);

        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        textView = findViewById(R.id.textView);

        //translate moves up and down

        //scale increases in size

        imageView5.animate().translationY(1050).setDuration(1500).setStartDelay(1500);

        imageViewbg.animate().translationY(-2500).setDuration(2000).setStartDelay(2000);
        imageViewbg.animate().scaleXBy(1).setDuration(1500).setStartDelay(1500);
        imageViewbg.animate().scaleYBy(1).setDuration(1500).setStartDelay(1500);
        imageView5.animate().scaleXBy(3).setDuration(1500).setStartDelay(1500);
        imageView5.animate().scaleYBy(3).setDuration(1500).setStartDelay(1500);

        textView.animate().translationY(-250).setDuration(3000).setStartDelay(3000);
        textView.animate().scaleYBy(2).setDuration(3000).setStartDelay(3000);
        textView.animate().scaleXBy(1).setDuration(3000).setStartDelay(3000);
        imageView6.animate().scaleYBy(2).setDuration(3000).setStartDelay(3000);
        imageView6.animate().scaleXBy(2).setDuration(3000).setStartDelay(3000);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this,
                                RegisterActivity.class));

                finish();

            }
        };
        Timer timer = new Timer();

        timer.schedule(timerTask,10000);

    }

}