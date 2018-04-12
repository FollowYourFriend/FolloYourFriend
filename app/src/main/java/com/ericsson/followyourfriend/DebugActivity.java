package com.ericsson.followyourfriend;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        final TextView text = (TextView) findViewById(R.id.textView);
        TextView text2 = (TextView) findViewById(R.id.text2);

        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(DebugActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DebugActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(DebugActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(DebugActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DebugActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(DebugActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            System.exit(0);

            return;
        }


        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                String temp = new String();
                temp += location.getProvider();
                temp += "   ";
                temp += String.valueOf(location.getLatitude());
                temp += "   ";
                temp += String.valueOf(location.getLongitude());
                text.setText((CharSequence) temp);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



        if(telephonyManager == null)
            text.setText("Shit");
        else if (telephonyManager.getAllCellInfo() == null)
            text.setText("Shit 2");
        else if (telephonyManager.getAllCellInfo().isEmpty())
            text.setText("Shit 3");
        else {
            String result = new String();
            for(CellInfo x : telephonyManager.getAllCellInfo()) {
                result += x.toString() + "\n----------------------------\n";
            }
            result = telephonyManager.getAllCellInfo().toString();

            if(text.toString() == "TextView")
                text.setText("Shit 5");

            //text.setText(result);
        }
        (new Thread(new Task(text2))).start();
    }
}
