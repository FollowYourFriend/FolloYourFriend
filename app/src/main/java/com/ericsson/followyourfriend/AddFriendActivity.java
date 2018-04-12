package com.ericsson.followyourfriend;

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

import static com.ericsson.followyourfriend.R.*;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_add_friend);
    }

    public void onClickAddFriend(View view) {


        FriendsManager m = (FriendsManager) GlobalManager.GetManager(ManagerEnum.FRIENDMANAGER);

        EditText name = findViewById (id.name);
        EditText surrname = findViewById (id.surrname);
        EditText phone = findViewById (id.phone);
        Friend f = new Friend(Integer.parseInt(phone.getText().toString()), name.getText().toString(), surrname.getText().toString());
        if(m != null){
            System.out.println("kopytko");
            m.addmFriends(f);}
        else
            System.out.println("kopytko2");

        Intent intent = new Intent(this,FriendActivity.class);
        startActivity(intent);
    }
}
