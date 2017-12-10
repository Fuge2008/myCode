package com.saas.saasuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.saas.saasuser.R;
import com.saas.saasuser.view.launcher.LauncherView;

public class SplashActivity extends BaseNoStatuBarActivity {
     LauncherView launcherView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       launcherView = (LauncherView) findViewById(R.id.load_view);

        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    launcherView.start();


                }
            },100);
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            },4000);


    }



    @Override
    public void doClick(View v) {

    }


}
