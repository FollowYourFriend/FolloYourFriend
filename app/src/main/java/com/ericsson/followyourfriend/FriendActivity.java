package com.ericsson.followyourfriend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;
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

import static android.provider.ContactsContract.*;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //ScrollView FriendList = (ScrollView) findViewById(R.id.FriendList);
        //FriendList.
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
