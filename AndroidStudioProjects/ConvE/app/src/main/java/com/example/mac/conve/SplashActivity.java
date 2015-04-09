package com.example.mac.conve;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

/**
 * Created by Peter Zolcer on 27/03/15.
 */

    public class SplashActivity extends MainActivity {

        /** Duration of wait **/

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.splash_activity); //set layout for splashscreen


                int Timer=1800; //Timer is set for 1,8seconds = time for running the splash screen
                     new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, Timer);
        }
    }



