package com.ericsson.followyourfriend;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Task implements Runnable {

    Task(TextView text) {
        this.text = text;
    }

    public String answer = new String();
    public TextView text;

    @Override
    public void run() {
        try {
            String serverAddress = "138.68.68.236";
            Socket s = new Socket(serverAddress, 9090);

            //Receiving message from server
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            answer = input.readLine();
            text.setText((CharSequence) answer);

            // Sending message to server
            BufferedWriter output =
                    new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            output.write("Yolo");
        } catch (Exception e) {
            Log.d("error",e.toString());
        }
    }
}
