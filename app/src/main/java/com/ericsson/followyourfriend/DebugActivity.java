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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ericsson.Person.Friend;

import java.util.ArrayList;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        final TextView text = (TextView) findViewById(R.id.textView);
        TextView text2 = (TextView) findViewById(R.id.text2);


        //ListView use
        ArrayList<Friend> friendsArray = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            friendsArray.add(new Friend(12344311, "Roman Romanowicz"));
            friendsArray.add(new Friend(60040304, "Jez Jerzy"));
        }


        TestAdapter adapter = new TestAdapter(this,friendsArray);

        ListView listView = (ListView) findViewById(R.id.friendsList);
        listView.setAdapter(adapter);





        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

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
