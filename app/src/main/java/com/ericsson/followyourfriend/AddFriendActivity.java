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



        FriendsManager m = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);

        EditText name = findViewById (id.name);
        EditText phone = findViewById (id.phone);
        Friend f = new Friend(Integer.parseInt(phone.getText().toString()), name.getText().toString());

        if(m.checkIfFriendNumberIsAlreadyOnList(f) == true){
            Intent intentAlreadyExist = new Intent(this,friendAlreadyExistActivity.class);
            startActivity(intentAlreadyExist);
            System.out.println(" Jestem tu!!!  ");
        }
        else{
            m.addmFriends(f);
            Intent intent = new Intent(this,FriendActivity.class);
            startActivity(intent);
        }
    }
}
