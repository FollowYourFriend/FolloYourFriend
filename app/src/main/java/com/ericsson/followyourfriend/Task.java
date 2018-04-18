package com.ericsson.followyourfriend;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

            //Sending message to server
            JSONObject jsonObject = new JSONObject();
            JSONObject reqJson = new JSONObject();
            jsonObject.put("Id", "791460336");
            jsonObject.put("Vis", "0");
            jsonObject.put("Lat", "52.2324");
            jsonObject.put("Lon", "19.123124");
            reqJson.put("Req",jsonObject);

            DataOutputStream os = null;
            os = new DataOutputStream(s.getOutputStream());
            os.writeBytes(reqJson.toString() + "\n");

            //Receiving message from server
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            answer = input.readLine();
            text.setText((CharSequence) answer);

            JSONArray jsonFriend;

            //os.close();
            //input.close();


            /*DataInputStream is = null;
            is = new DataInputStream(s.getInputStream());
            answer = is.readLine();
            text.setText((CharSequence) answer);
            is.close();*/

            System.out.println(answer);


        } catch (Exception e) {
            Log.d("error",e.toString());
        }
    }
}
