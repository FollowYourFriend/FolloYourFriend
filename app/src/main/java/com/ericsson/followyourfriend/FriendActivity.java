package com.ericsson.followyourfriend;

import android.Manifest;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.Console;
import java.io.FileOutputStream;
import java.io.IOException;
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

    FileOutputStream outputStream;
    String filename = "userData";
    String temp = "";
    String separator = ":";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        FriendsManager m = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);

        TestAdapter adapter = new TestAdapter(this,m.getFriends());

        ListView listView = (ListView) findViewById(R.id.firendListFriend);
        listView.setAdapter(adapter);

/*
        listView.setOnItemClickListener(new ArrayAdapter<?>().OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos,   long id) {
                Toast.makeText(getApplicationContext(),
                        helloPhrases[pos],
                        Toast.LENGTH_SHORT).show();
            }
        });*/

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

    public void onClickImport(View view) throws IOException {
        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        permissionManager.requestForPermission(new String[] {READ_CONTACTS, WRITE_CONTACTS},this);



        outputStream = openFileOutput(filename, Context.MODE_APPEND);

        if(!permissionManager.isPermissionGranted(READ_CONTACTS) && !permissionManager.isPermissionGranted(WRITE_CONTACTS))
            System.exit(0);

        FriendsManager m = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);

        Cursor phones = getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null,null,null, CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(CommonDataKinds.Phone.NUMBER));

            if(phoneNumber.contains("+48")){
                phoneNumber = phoneNumber.replaceAll("^[+0-9]{3}", "");
            }
            phoneNumber = phoneNumber.replaceAll("-", "");
            phoneNumber = phoneNumber.replaceAll(" ", "");
            if(!phoneNumber.equals("")) {
                System.out.println("   phone:" + phoneNumber + ":   name:" + name + ":");
                Friend f = new Friend(Integer.parseInt(phoneNumber), name);
                if (m.checkIfFriendNumberIsAlreadyOnList(f) == true) {
                    System.out.println(name + "   " + phoneNumber);
                } else {
                    m.addmFriends(f);
                    outputStream.write(Integer.toString(f.getmNumber()).getBytes());
                    outputStream.write(separator.getBytes());
                    outputStream.write(f.getmName().getBytes());
                    outputStream.write(separator.getBytes());
                }
            }
        }

        TestAdapter adapter = new TestAdapter(this,m.getFriends());

        ListView listView = (ListView) findViewById(R.id.firendListFriend);
        listView.setAdapter(adapter);

        phones.close();
    }
}