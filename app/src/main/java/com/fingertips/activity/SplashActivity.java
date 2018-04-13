package com.fingertips.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;


import com.fingertips.R;
import com.fingertips.helper.XClass;

import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        final Animation rotate, fadeIn;
        final XClass obj;

        obj = new XClass(SplashActivity.this);
        str = obj.getFromSharedPref(R.string.auth_token, "string", "").toString();
       /* rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        divider.startAnimation(fadeIn);
        brandName.startAnimation(fadeIn);
        appType.startAnimation(fadeIn);
        logo.startAnimation(rotate);*/


        //new FCM().updateToken(SplashScreen.this, null);

        Thread timerThread = new Thread() {
            public void run() {
                Intent intent;
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //rotate.cancel();
                    if (str.equalsIgnoreCase("")) {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
}
