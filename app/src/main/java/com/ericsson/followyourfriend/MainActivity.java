package com.ericsson.followyourfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.ericsson.managers.ManagerEnum.FRIENDMANAGER;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    String filename = "userData";
    String temp = "";
    File file;
    FileInputStream inputStream;
    String separator = ":";
    AtomicReference<JSONArray> json = new AtomicReference<>();
    AtomicBoolean atomicBooleanMain = new AtomicBoolean(false);
    AtomicBoolean atomicBooleanTask = new AtomicBoolean(true);

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                JSONObject jsonObject = new JSONObject(msg.getData().getString("jsonObject"));
                updateFriend(((FriendsManager) GlobalManager.getInstance().GetManager(FRIENDMANAGER)).getFriend(msg.getData().getInt("Num")), jsonObject);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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

        final FriendsManager m = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);
        int i;
        char a;

        file = new File(getApplicationContext().getFilesDir(), filename);
        try {
            inputStream = openFileInput(filename);
            while((i = inputStream.read())!=-1) {
                // converts integer to character
                a = (char)i;

                // prints character
                System.out.print(a);
                temp = temp + a;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] split1 = temp.split(separator);
        int j = 0;

        if(split1.length > 1){
            while(j < split1.length){
                m.getFriends().add(new Friend(Integer.parseInt(split1[j]),split1[j+1]));
                j = j + 2;
            }
        }

        //TODO: refactoring
        // New thread
        final TextView text2 = (TextView) findViewById(R.id.textView5);
        Thread task = new Thread(new Task(json, atomicBooleanMain, atomicBooleanTask));
        task.start();

        // Define the code block to be executed
        Thread t = new Thread() {
            public void run() {
                // Insert custom code here


                while(true)
                {
                    if(atomicBooleanMain.get() == false)
                        continue;
                    else{
                        atomicBooleanMain.set(false);
                        if (json != null) {

                            try {
                                Thread.sleep(1000);
                                for(int i = 0; i < json.get().length(); i++) {
                                    JSONObject object = json.get().getJSONObject(i);
                                    int phoneNr = Integer.valueOf(object.getString("Id"));
                                    Friend friend = m.getFriend(phoneNr);
                                    if (friend != null) {
                                        Message msg = handler.obtainMessage();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("Num", friend.getmNumber());
                                        bundle.putString("jsonObject",object.toString());
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        atomicBooleanTask.set(true);
                    }

                }
            }
        };
        t.start();

    }

    private void updateFriend(Friend friend, JSONObject json) throws JSONException {
        friend.setmLatitude(Double.valueOf(json.getString("Lat")));
        friend.setmLongitude(Double.valueOf(json.getString("Lon")));
        if(friend.getmMarker() != null) {
            friend.getmMarker().setPosition(new LatLng(friend.getmLatitude(), friend.getmLongitude()));
            String status = json.getString("Vis");
            friend.setmStatus(status.equals("0") ? VisibilityStatus.VISIBLE : status.equals("1") ? VisibilityStatus.INVISIBLE : VisibilityStatus.NOT_REGISTERED);
            if (status.equals("0") && friend.getmMarker().isVisible() == false)
                friend.getmMarker().setVisible(true);
            else if (!status.equals("0"))
                friend.getmMarker().setVisible(false);
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
                        .title(new String(friend.getmName()) + " " + String.valueOf(friend.getmNumber()))));

                if(friend.getmStatus() == VisibilityStatus.INVISIBLE)
                    friend.getmMarker().setVisible(false);
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
//        if(phoneNr.length() >= minNrLength){
//            User.getInstance().setmPhoneNr(phoneNr);
//        //get IMSI nr
//        //User.getInstance().setmPhoneNr(tMgr.getSubscriberId());
//        }

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
                    User.getInstance().setmLatitude(location.getLatitude());
                    User.getInstance().setmLongitude(location.getLongitude());
                }

                if(!User.getInstance().getmMarker().isVisible())
                    User.getInstance().getmMarker().setVisible(true);

                if(!User.getInstance().isLocationValid())
                    User.getInstance().setLocationValid(true);
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
