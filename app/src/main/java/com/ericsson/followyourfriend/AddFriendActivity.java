package com.ericsson.followyourfriend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ericsson.Person.Friend;
import com.ericsson.managers.FriendsManager;
import com.ericsson.managers.GlobalManager;
import com.ericsson.managers.Manager;
import com.ericsson.managers.ManagerEnum;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.ericsson.followyourfriend.R.*;

public class AddFriendActivity extends AppCompatActivity {

    FileOutputStream outputStream;
    String filename = "userData";
    String temp = "";
    String separator = ":";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_add_friend);
    }

    public void onClickAddFriend(View view) throws IOException, FileNotFoundException {

        outputStream = openFileOutput(filename, Context.MODE_APPEND);

        FriendsManager m = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);

        EditText name = findViewById (id.name);
        EditText phone = findViewById (id.phone);
        if((!phone.getText().toString().equals("")) && (!name.getText().toString().equals(""))) {
            Friend f = new Friend(Integer.parseInt(phone.getText().toString()), name.getText().toString());
            System.out.println("\nWszedłem\n");

            if (m.checkIfFriendNumberIsAlreadyOnList(f) == true) {
                Intent intentAlreadyExist = new Intent(this, friendAlreadyExistActivity.class);
                startActivity(intentAlreadyExist);
                System.out.println(" Jestem tu!!!  ");
            } else {
                outputStream.write(Integer.toString(f.getmNumber()).getBytes());
                outputStream.write(separator.getBytes());
                outputStream.write(f.getmName().getBytes());
                outputStream.write(separator.getBytes());
                m.addmFriends(f);
                Intent intent = new Intent(this, FriendActivity.class);
                startActivity(intent);
            }
        }
    }
}