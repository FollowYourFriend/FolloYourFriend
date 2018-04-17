package com.ericsson.followyourfriend;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ericsson.Person.User;

public class AskPhoneNr extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_phone_nr);
    }

    public void onClickAddNumber(View view) {
        EditText number = (EditText) findViewById(R.id.userNr);
        String phoneNr = number.getText().toString();

        System.out.println(phoneNr);

        if(phoneNr != "") {
            User.getInstance().setmPhoneNr(phoneNr.toString());
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(getString(R.string.my_number), phoneNr);
        edit.commit();

        finish();
    }
}
