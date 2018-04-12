package com.ericsson.followyourfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;

import Person.Friend;

public class FriendActivity extends AppCompatActivity {

    private ArrayList<Friend> mFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        ScrollView FriendList = (ScrollView) findViewById(R.id.FriendList);
        //FriendList.
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(this,AddFriend.class);
        startActivity(intent);
    }
}
