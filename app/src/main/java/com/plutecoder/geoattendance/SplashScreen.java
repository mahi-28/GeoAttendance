package com.plutecoder.geoattendance;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import utils.PreferenceHelper;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3500);
//                  sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {


                    if (!PreferenceHelper.GetIntroShown(SplashScreen.this)) {
                        Intent intent = new Intent(SplashScreen.this, IntroScreenGeo.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {


                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // User is signed in.
                            if (PreferenceHelper.getusername(SplashScreen.this).equals("Admin")) {
                                Intent i = new Intent(SplashScreen.this, AdminActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                SplashScreen.this.finish();
                            } else {
                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                SplashScreen.this.finish();
                            }
                        } else {
                            // No user is signed in.

                            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            SplashScreen.this.finish();
                        }
                    }
                }
            }
        };
        timer.start();

    }
}
