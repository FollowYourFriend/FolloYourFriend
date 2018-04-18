package com.ericsson.followyourfriend;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.Console;
import java.io.FileNotFoundException;
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
    View previousView = null;
    FriendsManager friendsManager;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        friendsManager = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);
        createListView();
        setPosition(999);
    }

    private void createListView(){
        TestAdapter adapter = new TestAdapter(this,friendsManager.getFriends());

        final ListView listView = (ListView) findViewById(R.id.firendListFriend);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos,   long id) {
                if(previousView != null){
                    previousView.setBackgroundColor(Color.argb(255, 250, 250, 250));
                }
                v.setBackgroundColor(Color.argb(255, 236, 239, 255));
                previousView = v;
                setPosition(pos);

            }
        });
    }

    private void setPosition(int i){
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(getString(R.string.position_selected_friend), Integer.toString(i));
        System.out.println("\nPozycja do zapisu:"+ Integer.toString(i));
        edit.commit();
    }

    private String getPosition(){
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        System.out.println("\nPozycja:"+ prefs.getString(getString(R.string.position_selected_friend), ""));
        return prefs.getString(getString(R.string.position_selected_friend), "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        //Show dialog window with message that permission is necessary
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(this,AddFriendActivity.class);
        startActivity(intent);
    }

    public void onClickEdit(View view) {
        Intent intent = new Intent(this,EditFriendActivity.class);
        startActivity(intent);
    }

    private void saveFriendsToFile(ArrayList<Friend> friends) throws IOException {
        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

        for(Friend f : friends) {
            outputStream.write(Integer.toString(f.getmNumber()).getBytes());
            outputStream.write(separator.getBytes());
            outputStream.write(f.getmName().getBytes());
            outputStream.write(separator.getBytes());
        }
    }

    public void onClickImport(View view) throws IOException {
        PermissionManager permissionManager = (PermissionManager) GlobalManager.getInstance().GetManager(ManagerEnum.PERMISSIONMANAGER);
        permissionManager.requestForPermission(new String[] {READ_CONTACTS, WRITE_CONTACTS},this);

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
                }
            }
        }
        saveFriendsToFile(m.getFriends());

        TestAdapter adapter = new TestAdapter(this,m.getFriends());

        ListView listView = (ListView) findViewById(R.id.firendListFriend);
        listView.setAdapter(adapter);

        phones.close();
    }

    public void onClickDelete(View view) throws IOException {
        ArrayList<Friend> temp;
        int pos = Integer.parseInt(getPosition());
        if(pos != 999){
            temp = friendsManager.getFriends();
            temp.remove(pos);
            friendsManager.setFriends(temp);
            saveFriendsToFile(temp);
        }
        setPosition(999);
        createListView();
    }
}