package com.ericsson.followyourfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }

    public void onClickAddFriend(View view) {
        Intent intent = new Intent(this,FriendActivity.class);



        startActivity(intent);
    }
}
