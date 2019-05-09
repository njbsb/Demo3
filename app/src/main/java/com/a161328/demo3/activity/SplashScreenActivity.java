package com.a161328.demo3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a161328.demo3.R;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView ic_logo;
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ic_logo = findViewById(R.id.ic_splash_logo);
        tv_title = findViewById(R.id.tv_splash_title);

        Animation mAnim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        ic_logo.startAnimation(mAnim);
        tv_title.startAnimation(mAnim);

        final Intent home = new Intent(this, MainActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(home);
                    finish();
                }
            }
        };
        timer.start();
    }
}
