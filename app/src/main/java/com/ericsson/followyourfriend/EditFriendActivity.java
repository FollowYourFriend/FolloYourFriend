package com.ericsson.followyourfriend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ericsson.Person.Friend;
import com.ericsson.managers.FriendsManager;
import com.ericsson.managers.GlobalManager;
import com.ericsson.managers.ManagerEnum;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditFriendActivity extends AppCompatActivity {

    SharedPreferences prefs;
    EditText name;
    FriendsManager m;
    FileOutputStream outputStream;
    String separator = ":";
    String filename = "userData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        m = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);

        name = findViewById (R.id.nameEdit);
        name.setText(m.getFriends().get(Integer.parseInt(getPosition())).getmName());

    }

    public void onClickUpdate(View view) throws IOException {
        ArrayList<Friend> temp = m.getFriends();
        if(Integer.parseInt(getPosition()) != 999)
            temp.get(Integer.parseInt(getPosition())).setmName(name.getText().toString());

        System.out.println("\nNAME:"+temp.get(Integer.parseInt(getPosition())).getmName());
        saveFriendsToFile(temp);
        m.setFriends(temp);

        setPosition(999);
        Intent intent = new Intent(this,FriendActivity.class);
        startActivity(intent);
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


    private void saveFriendsToFile(ArrayList<Friend> friends) throws IOException {
        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

        for(Friend f : friends) {
            outputStream.write(Integer.toString(f.getmNumber()).getBytes());
            outputStream.write(separator.getBytes());
            outputStream.write(f.getmName().getBytes());
            outputStream.write(separator.getBytes());
        }
    }
}
