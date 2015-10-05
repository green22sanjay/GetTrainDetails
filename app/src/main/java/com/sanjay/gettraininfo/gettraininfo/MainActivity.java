package com.sanjay.gettraininfo.gettraininfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sanjay.gettraininfo.gettraininfo.ModelClass.Destinationitems;
import com.sanjay.gettraininfo.gettraininfo.SqlLiteHandler.DBHelper;
import com.sanjay.gettraininfo.gettraininfo.services.GPSTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    private GoogleMap mMap;
    private DBHelper mydb ;
    private TableRow searchTableLayout;
    private TextView getSource,getDEstination;
    private Toolbar toolbar;
    SharedPreferences pref;
    private List<Destinationitems> mCategoryfeeditems ;
    private Destinationitems retunitems ;
    FloatingActionButton fab;

    Marker newmarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);

        getSource= (TextView)findViewById(R.id.yoursource);
        getDEstination = (TextView)findViewById(R.id.pickyoursource);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchTableLayout = (TableRow)findViewById(R.id.searchtable);


        fab = (FloatingActionButton) findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchTableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_to_destination = new Intent(MainActivity.this, DestinationAdd.class);
                move_to_destination.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(move_to_destination);
            }
        });
        pref = getApplicationContext().getSharedPreferences("Adddestination", 0);
        if(pref.getString("destinationname", null)!=null) {
            String getloginid = pref.getString("destinationname", null);
            getDEstination.setText(getloginid);

        }else {
            Snackbar.make(MainActivity.this.getWindow().getDecorView(),"null", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        String city = null;



        GPSTracker gps = new GPSTracker(this);

        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
            newmarker = googleMap.addMarker(new MarkerOptions().position(sydney));
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {

                    getSource.setText(addresses.get(0).getLocality());
                    city= addresses.get(0).getLocality();
                    try {
                        mCategoryfeeditems = new ArrayList<Destinationitems>();
                        Destinationitems catfeeditems = new Destinationitems();
                        catfeeditems.setId(1);
                        catfeeditems.setDestinationname(city);
                        mCategoryfeeditems.add(catfeeditems);

                        Destinationitems df = mCategoryfeeditems.get(0);

                        Destinationitems rtncatitems = new Destinationitems();
                        retunitems = mydb.getnearstname(df.getDestinationname());

                        if (!retunitems.getDestinationname().isEmpty()) {
                            String destina = retunitems.getDestinationname();
                            getSource.setText(destina);
                            Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + destina, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            getSource.setText(city);
                            Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + city, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }catch (Exception er){
                        getSource.setText(city);
                        Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + city, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }






                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        if(city==null){
            newmarker.setPosition(sydney);
            newmarker.setTitle("Marker in Loading..");
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Loading.."));
        }else {
            newmarker.setPosition(sydney);
            newmarker.setTitle("Marker in " + city);
           // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in "+ city));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,200));

            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    LatLng centerOfMap = mMap.getCameraPosition().target;
                    Log.v("newlatlongs", String.valueOf(centerOfMap));
                    newmarker.setPosition(centerOfMap);
                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    double newlatitude = centerOfMap.latitude;
                    double newlognitude = centerOfMap.longitude;
                    String newcity = null;
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(newlatitude, newlognitude, 1);
                        if (addresses.size() > 0) {

                            getSource.setText(addresses.get(0).getLocality());
                            newcity = addresses.get(0).getLocality();
                            try {
                                mCategoryfeeditems = new ArrayList<Destinationitems>();
                                Destinationitems catfeeditems = new Destinationitems();
                                catfeeditems.setId(1);
                                catfeeditems.setDestinationname(newcity);
                                mCategoryfeeditems.add(catfeeditems);

                                Destinationitems df = mCategoryfeeditems.get(0);

                                Destinationitems rtncatitems = new Destinationitems();
                                retunitems = mydb.getnearstname(df.getDestinationname());

                                if (!retunitems.getDestinationname().isEmpty()) {
                                    String destina = retunitems.getDestinationname();
                                    getSource.setText(destina);
                                    Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + destina, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    getSource.setText(newcity);
                                    Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + newcity, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            } catch (Exception er) {
                                getSource.setText(newcity);
                                Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + newcity, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }


                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String city = null;

                    GPSTracker gps = new GPSTracker(MainActivity.this);


                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();


                        // Add a marker in Sydney and move the camera
                        LatLng sydney = new LatLng(latitude, longitude);
                        newmarker.setPosition(sydney);
                        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());

                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(latitude, longitude, 1);
                            if (addresses.size() > 0) {

                                getSource.setText(addresses.get(0).getLocality());
                                city = addresses.get(0).getLocality();
                                try {
                                    mCategoryfeeditems = new ArrayList<Destinationitems>();
                                    Destinationitems catfeeditems = new Destinationitems();
                                    catfeeditems.setId(1);
                                    catfeeditems.setDestinationname(city);
                                    mCategoryfeeditems.add(catfeeditems);

                                    Destinationitems df = mCategoryfeeditems.get(0);

                                    Destinationitems rtncatitems = new Destinationitems();
                                    retunitems = mydb.getnearstname(df.getDestinationname());

                                    if (!retunitems.getDestinationname().isEmpty()) {
                                        String destina = retunitems.getDestinationname();
                                        getSource.setText(destina);
                                        Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + destina, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    } else {
                                        getSource.setText(city);
                                        Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + city, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                } catch (Exception er) {
                                    getSource.setText(city);
                                    Snackbar.make(MainActivity.this.getWindow().getDecorView(), "Your Source is : " + city, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }


                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (city == null) {
                            newmarker.setPosition(sydney);
                            newmarker.setTitle("Marker in Loading..");
                            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Loading.."));
                        } else {
                            newmarker.setPosition(sydney);
                            newmarker.setTitle("Marker in " + city);
                            // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in "+ city));
                        }

                    } else {
                        gps.showSettingsAlert();
                    }

                }
            });

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();


        }


    }





}
