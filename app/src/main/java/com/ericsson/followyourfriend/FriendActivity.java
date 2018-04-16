package com.ericsson.followyourfriend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import java.util.ArrayList;

import com.ericsson.Person.Friend;
import com.ericsson.managers.FriendsManager;
import com.ericsson.managers.GlobalManager;
import com.ericsson.managers.ManagerEnum;
import com.ericsson.managers.PermissionManager;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.ContactsContract.*;

public class FriendActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //ScrollView FriendList = (ScrollView) findViewById(R.id.FriendList);
        //FriendList.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        //Show dialog window with message that permission is necessary
    }

    public void addFriendToList() {

    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(this,AddFriendActivity.class);
        startActivity(intent);
    }

    public void onClickEdit(View view) {
        Intent intent = new Intent(this,EditFriendActivity.class);
        startActivity(intent);
    }

    public void onClickImport(View view) {
        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        permissionManager.requestForPermission(new String[] {READ_CONTACTS, WRITE_CONTACTS},this);

        if(!permissionManager.isPermissionGranted(READ_CONTACTS) && !permissionManager.isPermissionGranted(WRITE_CONTACTS))
            System.exit(0);

        FriendsManager m = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Friend f = new Friend(Integer.parseInt(phoneNumber), name);
            m.addmFriends(f);
            System.out.println(name + "   " + phoneNumber);
        }
        phones.close();
    }
}
