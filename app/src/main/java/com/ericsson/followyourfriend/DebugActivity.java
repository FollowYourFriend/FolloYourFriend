package com.ericsson.followyourfriend;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ericsson.Person.Friend;
import com.ericsson.Person.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;

public class DebugActivity extends AppCompatActivity implements OnMapReadyCallback {

    double mLatitude = 52.06516;
    double mLongitude = 19.25252;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        final TextView text = (TextView) findViewById(R.id.textView);
        TextView text2 = (TextView) findViewById(R.id.text2);
        final TextView text3 = (TextView) findViewById(R.id.text3);



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



        //MapView use
        MapFragment mMapFragment = MapFragment.newInstance();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(0,mMapFragment);
        fragmentTransaction.commit();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //Location
        final TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mMarker.setPosition(new LatLng(mLatitude,mLongitude));
                // Called when a new location is found by the network location provider.
                String temp = new String();
                temp += location.getProvider();
                temp += "   ";
                temp += String.valueOf(location.getLatitude());
                temp += "   ";
                temp += String.valueOf(location.getLongitude());
                text.setText((CharSequence) temp);
                if (telephonyManager != null)
                    User.getInstance().setmPhoneNr(telephonyManager.getLine1Number());
                text3.setText((CharSequence) (telephonyManager.getLine1Number().toString()));
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
        }



        //Client Server interactions
        //(new Thread(new Task(text2))).start();


        text3.setText(User.getInstance().getmPhoneNr() + " " + User.getInstance().getmName());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mLatitude,mLongitude))
                .title(User.getInstance().getmName() + " " + User.getInstance().getmPhoneNr())
                .icon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE)));
    }
}
