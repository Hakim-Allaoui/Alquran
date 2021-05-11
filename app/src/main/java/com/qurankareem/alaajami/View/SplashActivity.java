package com.qurankareem.alaajami.View;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qurankareem.alaajami.R;
import com.qurankareem.alaajami.utils.Tools;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Activity ac = this;
        //creating new thread just for demonstration of background tasks
        Thread t=new Thread() {
            public void run() {

                try {
                    //sleep thread for 10 seconds
//                    sleep(10000);

                    //Call Main activity
                    /*Intent i=new Intent(SplashActivity.this, com.qurankareem.alaajami.View.MainActivity.class);
                    startActivity(i);*/

                    Tools.fetchAdNetwork(ac);
                    //destroying Splash activity
                    finish();

                } catch (Exception e) {
//                    e.printStackTrace();
                    Log.v( "Ads", "adNetwork :" + e.getMessage());
                    Intent i=new Intent(SplashActivity.this, com.qurankareem.alaajami.View.MainActivity.class);
                    startActivity(i);
                }
            }
        };

        //start thread
        t.start();
    }
}