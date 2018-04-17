package com.ericsson.followyourfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;


import com.ericsson.Person.Friend;
import com.ericsson.Person.User;
import com.ericsson.Person.VisibilityStatus;
import com.ericsson.managers.FriendsManager;
import com.ericsson.managers.GlobalManager;
import com.ericsson.managers.ManagerEnum;
import com.ericsson.managers.PermissionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.ericsson.managers.ManagerEnum.FRIENDMANAGER;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User.getInstance().init();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        User.getInstance().setmPhoneNr(prefs.getString(getString(R.string.my_number),""));
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            requestUserNumber();
        }

        GlobalManager.getInstance().init();

        setContentView(R.layout.activity_main);

        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        permissionManager.requestForPermission(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE}, this);

        if (permissionManager.isPermissionGranted(ACCESS_COARSE_LOCATION) && permissionManager.isPermissionGranted(ACCESS_FINE_LOCATION) &&
                permissionManager.isPermissionGranted(READ_PHONE_STATE)) {
            initUserData();
            initMap();
            initLocation();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        User.getInstance().setmMarker(googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0,0))
                .title(User.getInstance().getmName() + " " + User.getInstance().getmPhoneNr())
                .icon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE))));

        User.getInstance().getmMarker().setVisible(false);

        FriendsManager friendsManager = (FriendsManager) GlobalManager.getInstance().GetManager(FRIENDMANAGER);
        for(Friend friend: friendsManager.getFriends())
        {
            if(friend.getmStatus() != VisibilityStatus.NOT_REGISTERED) {
                friend.setmMarker(googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(friend.getmLatitude(), friend.getmLongitude()))
                        .title(friend.getmName())));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);

        while(!permissionManager.isPermissionGranted(ACCESS_COARSE_LOCATION) || !permissionManager.isPermissionGranted(ACCESS_FINE_LOCATION) ||
                !permissionManager.isPermissionGranted(READ_PHONE_STATE))
            System.exit(0);

        initUserData();
        initMap();
        initLocation();
    }

    public void onClickDebug(View view) {
        Intent intent = new Intent(this,DebugActivity.class);
        startActivity(intent);
    }

    public void onClickFriend(View view) {
        Intent intent = new Intent(this,FriendActivity.class);
        startActivity(intent);
    }

    private void requestUserNumber() {
        Intent intent = new Intent(this,AskPhoneNr.class);
        startActivity(intent);
    }

    private boolean initUserData() {
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNr = new String("");
        if (tMgr != null)
            phoneNr = tMgr.getLine1Number();

        int minNrLength = 5;
        if(phoneNr.length() >= minNrLength){
            User.getInstance().setmPhoneNr(phoneNr);
        //get IMSI nr
        //User.getInstance().setmPhoneNr(tMgr.getSubscriberId());
        }

        return true;
    }

    private boolean initMap() {
        MapFragment mMapFragment = MapFragment.newInstance();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(0,mMapFragment);
        fragmentTransaction.commit();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return true;
    }

    private void initLocation()
    {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if(User.getInstance().getmMarker() != null) {
                    User.getInstance().getmMarker().setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                }

                if(!User.getInstance().getmMarker().isVisible())
                    User.getInstance().getmMarker().setVisible(true);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
