package com.example.rahul.dustbin;

import android.content.Context;
import android.content.Intent;
import android.database.DefaultDatabaseErrorHandler;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static android.R.attr.name;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView add_location;
    String name1,email;
    private GPSTracker gpsTracker;
    private Location mLocation;
    public double latitude,longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        add_location = (TextView)findViewById(R.id.add_location);
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendAltitude(email,latitude, longitude);
            }
        });




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


       // Toast.makeText(getApplicationContext(),"name is :"+name + " email is : "+email,Toast.LENGTH_LONG).show();

//        Intent myIntent = getIntent();
//        if (myIntent.hasExtra("name"))
//        name1=myIntent.getStringExtra("name");
//        if (myIntent.hasExtra("email"))
//            email=myIntent.getStringExtra("email");


        Bundle bundle = getIntent().getExtras();
        name1 = bundle.getString("name");
        email = bundle.getString("email");
        new MyAsync().execute(name1,email);
    }




//    private class MyAsync extends AsyncTask<String,String,String>{
//
//       MyAsync(){}
//
//        @Override
//        protected String doInBackground(String... params) {
//
//
//            String u = "http://5dbccef3.ngrok.io/getinfo";
//            try {
//                URL url=new URL(u);
//                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setDoOutput(true);
//               // connection.connect();
//                OutputStream os=connection.getOutputStream();
//
//                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
//                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode("shikari","UTF-8")+"&"
//                        + URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode("pri@gmail.com","UTF-8")+"&"
//                        + URLEncoder.encode("lat","UTF-8")+"="+URLEncoder.encode("542","UTF-8")+"&"
//                        + URLEncoder.encode("lon","UTF-8")+"="+URLEncoder.encode("652","UTF-8");
//
//
//                writer.write(data);
//                writer.flush();
//                writer.close();
//
//                os.close();
//                InputStream is=connection.getInputStream();
//                is.close();
//                return "success";
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String p) {
//          //  super.onPostExecute(p);
//            Toast.makeText(getApplicationContext(),p,Toast.LENGTH_LONG).show();
//        }
//    }


    private class MyAsync extends AsyncTask<String,String,String>{
        MyAsync( ){


        }

        @Override
        protected String doInBackground(String... params) {

            //if(params.length==2)
            String u = "http://5dbccef3.ngrok.io/data?name=rahul&mail=12@";
            try {
                URL url=new URL(u);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                InputStream os=connection.getInputStream();


                BufferedReader reader=new BufferedReader(new InputStreamReader(os));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line=reader.readLine())!=null){
                    buffer.append(line);
                }

                Log.e("what the fuck",buffer.toString());



                reader.close();
                os.close();
//                InputStream is=connection.getInputStream();
//                is.close();
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String p) {
            //  super.onPostExecute(p);
            //Toast.makeText(getApplicationContext(),"name is :"+name + " email is : "+email,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),p,Toast.LENGTH_LONG).show();
        }
    }

    public void sendAltitude(String email,double latitude, double longitude){

    }


//    URL url = new URL("http://..";
//    HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
//httpCon.setRequestMethod("POST"; //it's a post request
//httpCon.setDoInput(true); //read response
//httpCon.setDoOutput(true); //send Post body
//... = httpCon.getOutputStream(); //Here you go, do whatever you want with this stream
}
