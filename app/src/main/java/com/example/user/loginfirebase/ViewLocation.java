package com.example.user.loginfirebase;

import android.Manifest;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ViewLocation extends AppCompatActivity {

    private DatabaseReference mRootRef;
    private Button getLocation,stopGPS;
    private TextView timerCounting;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private int counter = 1;
    GPSTracker gps;

    Handler h = new Handler();
    Thread task;
    private long startTime;
    private String timeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        try{
           if (ActivityCompat.checkSelfPermission(this,mPermission) != PERMISSION_GRANTED)
           {
               ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        getLocation = (Button)findViewById(R.id.getLocationBtn);
        stopGPS = (Button)findViewById(R.id.stopGPSBtn);
        timerCounting = (TextView)findViewById(R.id.timerCounting);

        mRootRef = FirebaseDatabase.getInstance().getReference("GPS_Location").push();

        stopGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopHandlerTask();
                timerCounting.setText("Location Service is Stopped.");
            }
        });
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                counter = 1;

            }
        });



    }

    public void showToastCurrentLocation(){
        gps = new GPSTracker(ViewLocation.this);
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            mRootRef.child("currentTIme :").setValue(System.currentTimeMillis()+"");
            mRootRef.child("latitude").setValue(latitude+"");
            mRootRef.child("longitude").setValue(longitude+"");
            Toast.makeText(getApplicationContext(),"Current location is \nLat:"+latitude+"\nLong:"+longitude,Toast.LENGTH_SHORT).show();
        }else{
            gps.showSettingsAlert();
        }
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        task  = new Thread(){
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                long secs = millis /1000 % 60; // second
    ////////////////////////////
                timeString = String.format("%02d",secs); //2digit
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerCounting.setText(timeString);
                    }
                });
                h.postDelayed(task,1000); //upadate every 1 second
                if(secs%5==0) showToastCurrentLocation();
//                    super.run();
            }
        };
        ///////////////////////
        h.postDelayed(task,1000);
    }

    public void stopHandlerTask(){
        h.removeCallbacks(task);
    }


}
