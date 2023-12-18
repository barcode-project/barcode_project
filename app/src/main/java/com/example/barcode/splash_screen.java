package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                boolean isLoggedIn = userPref.getBoolean("isLoggedIn", false);
                boolean isReset = userPref.getBoolean("isReset", false);
//                boolean isPlaced = userPref.getBoolean("isPlaced", false);
                String type = userPref.getString("type", "non");
                if (isLoggedIn) {
                    switch (type) {
                        case "user":
                            if(isReset) {
                                startActivity(new Intent(splash_screen.this, MainActivity.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(splash_screen.this, resetpass.class));
                                finish();
                            }

                        default:
                            // Do something when no option is selected
                            break;
                    }
                } else {
                    startActivity(new Intent(splash_screen.this, login_page.class));
                    finish();
                }
            }
        }, 2000);

    }

//    private void isFirstTime() {
//        //for checking if the app is running for the very first time
//        //we need to save a value to shared preferences
//        SharedPreferences preferences = getApplication().getSharedPreferences("onBoard", MODE_PRIVATE);
//        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
//        //default value true
//        if (isFirstTime) {
//            // if its true then its first time and we will change it false
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("isFirstTime", false);
//            editor.apply();
//
//            // start main activity
//            startActivity(new Intent(splash_screen.this, login_page.class));
//            finish();
//        } else {
//            //start login Activity
//            startActivity(new Intent(splash_screen.this, MainActivity.class));
//            finish();
//        }
//    }
}