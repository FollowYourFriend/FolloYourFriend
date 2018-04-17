package com.ericsson.followyourfriend;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.ericsson.Person.Friend;
import com.ericsson.Person.User;
import com.ericsson.managers.FriendsManager;
import com.ericsson.managers.GlobalManager;
import com.ericsson.managers.ManagerEnum;
import com.ericsson.managers.PermissionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.ericsson.managers.ManagerEnum.FRIENDMANAGER;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        GlobalManager.getInstance().init();
        User.getInstance().init();

        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        permissionManager.requestForPermission(new String[] {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},this);

        setContentView(R.layout.activity_main);

        initMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        User.getInstance().setmMarker(googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(52.06516,19.25252))
                .title("Marker Marker 600293912")));


        FriendsManager friendsManager = (FriendsManager) GlobalManager.getInstance().GetManager(FRIENDMANAGER);
        for(Friend friend: friendsManager.getFriends())
        {
            friend.setmMarker(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(friend.getmLatitude(),friend.getmLongitude()))
                    .title(friend.getmName())));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        permissionManager.killActivityIfPermissionIsNotGranted(grantResults);
    }

    public void onClickDebug(View view) {
        Intent intent = new Intent(this,DebugActivity.class);
        startActivity(intent);
    }

    public void onClickFriend(View view) {
        Intent intent = new Intent(this,FriendActivity.class);
        startActivity(intent);
    }

    private void initMap() {
        MapFragment mMapFragment = MapFragment.newInstance();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(0,mMapFragment);
        fragmentTransaction.commit();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
