package com.ericsson.followyourfriend;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.TextView;

import com.ericsson.managers.GlobalManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        GlobalManager.Init();
        setContentView(R.layout.activity_main);
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
