package com.sanjay.gettraininfo.gettraininfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sanjay.gettraininfo.gettraininfo.Appcontroler.AppController;
import com.sanjay.gettraininfo.gettraininfo.ModelClass.Destinationitems;
import com.sanjay.gettraininfo.gettraininfo.SqlLiteHandler.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class SplashScreen extends AppCompatActivity {
    private DBHelper mydb ;
    private static final boolean AUTO_HIDE = true;
    Intent move_tomainactivity;
    final String url = "https://cube26-1337.0x10.info/stations";
    private static final String TAG = DestinationAdd.class.getSimpleName();

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        mydb = new DBHelper(this);

        int count = mydb.numberOfRows();
        String stringcount = String.valueOf(count);
        Toast.makeText(getApplicationContext(),stringcount,Toast.LENGTH_LONG).show();
        if(count==0) {
            JsonArrayRequest jreq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {

                            parseJsonFeed(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            AppController.getInstance().addToRequestQueue(jreq, "jreq");
        }
        else {
            Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep for 5 seconds
                        sleep(1000);

                        // After 5 seconds redirect to another intent
                        move_tomainactivity = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(move_tomainactivity);

                        //Remove activity
                        finish();

                    } catch (Exception e) {

                    }
                }
            };

            // start thread
            background.start();

        }

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });


        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        delayedHide(100);
    }

    private void parseJsonFeed(JSONArray response) {
        try {

            String sanjaynama = String.valueOf(response.length());
            Log.v("sanjayda", sanjaynama);

            for (int i = 0; i < response.length(); i++) {
                Destinationitems itemsl = new Destinationitems();
                String names =response.getString(i);
                itemsl.setId(i);
                itemsl.setDestinationname(names);
                mydb.insertContact(names);

            }

            move_tomainactivity = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(move_tomainactivity);



        } catch (JSONException e) {
            e.printStackTrace();

        }

    }


    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
