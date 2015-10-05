package com.sanjay.gettraininfo.gettraininfo;

/**
 * Created by Dinesh on 10/4/2015.
 */
import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;

import com.sanjay.gettraininfo.gettraininfo.AdaptersClass.DestinationAdpter;
import com.sanjay.gettraininfo.gettraininfo.Appcontroler.AppController;
import com.sanjay.gettraininfo.gettraininfo.ModelClass.Destinationitems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DestinationAdd extends AppCompatActivity {
    EditText inputSearch;
    private ListView listView;
    Activity mActivity;
    private DestinationAdpter catAdapter;
    SharedPreferences pref; // 0 - for private mode

    private List<Destinationitems> mCategoryfeeditems ;
    final String url = "https://cube26-1337.0x10.info/stations";
    private static final String TAG = DestinationAdd.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* Set activity layout */
        setContentView(R.layout.activity_destination);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception er){
            Log.v("destinationbar", String.valueOf(er));
        }
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle("your");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /* Initialize listView */
        listView = (ListView) findViewById(R.id.listView);


        mCategoryfeeditems = new ArrayList<Destinationitems>();
        catAdapter = new DestinationAdpter(this, mCategoryfeeditems);

        listView.setClickable(true);



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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Destinationitems catfeeditems = mCategoryfeeditems.get(position);
                pref = getApplicationContext().getSharedPreferences("Adddestination", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("destinationname", catfeeditems.getDestinationname());
                editor.apply();

                Intent movetomain = new Intent(DestinationAdd.this,MainActivity.class);
                startActivity(movetomain);
            }
        });



}


    private void parseJsonFeed(JSONArray response) {
        try {

            if (null == mCategoryfeeditems) {
                mCategoryfeeditems = new ArrayList<Destinationitems>();
            }
            String sanjaynama = String.valueOf(response.length());
            Log.v("sanjayda", sanjaynama);

            for (int i = 0; i < response.length(); i++) {
                Destinationitems itemsl = new Destinationitems();
                String names =response.getString(i);
                itemsl.setId(i);
                itemsl.setDestinationname(names);
                mCategoryfeeditems.add(itemsl);

            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
        listView.setAdapter(catAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}