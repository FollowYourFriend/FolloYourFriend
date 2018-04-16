package com.ericsson.followyourfriend;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.ericsson.managers.GlobalManager;
import com.ericsson.managers.ManagerEnum;
import com.ericsson.managers.PermissionManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        GlobalManager.getInstance().init();

        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        permissionManager.requestForPermission(new String[] {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},this);

        setContentView(R.layout.activity_main);
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


}
