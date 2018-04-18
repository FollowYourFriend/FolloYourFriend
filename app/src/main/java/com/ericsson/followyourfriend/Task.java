package com.ericsson.followyourfriend;

import android.util.Log;
import android.widget.TextView;

import com.ericsson.Person.Friend;
import com.ericsson.Person.VisibilityStatus;
import com.ericsson.managers.FriendsManager;
import com.ericsson.managers.GlobalManager;
import com.ericsson.managers.ManagerEnum;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public class Task implements Runnable {

    Task(AtomicReference<JSONObject> json, AtomicBoolean atM, AtomicBoolean atT) {
        this.json = json;
        atomicBooleanMain = atM;
        atomicBooleanTask = atT;
    }

    public String answer = new String();
    AtomicReference<JSONObject> json ;
    public AtomicBoolean atomicBooleanMain;
    public AtomicBoolean atomicBooleanTask;

    @Override
    public void run() {
        try {
            final String serverAddress = "138.68.68.236";

            while(true)
            {
                if(atomicBooleanTask.get() == false)
                    continue;
                else {
                    atomicBooleanTask.set(false);
                    final Socket s = new Socket(serverAddress, 9090);
                    //Sending message to server
                    JSONObject jsonObject = new JSONObject();
                    JSONObject reqJson = new JSONObject();
                    jsonObject.put("Id", "667770894");
                    jsonObject.put("Vis", "0");
                    jsonObject.put("Lat", "52.27");
                    jsonObject.put("Lon", "19.13");
                    reqJson.put("Update_Req", jsonObject);

                    DataOutputStream os = null;
                    os = new DataOutputStream(s.getOutputStream());
                    os.writeBytes(reqJson.toString() + "\n");

                    //Receiving message from server
                    BufferedReader input =
                            new BufferedReader(new InputStreamReader(s.getInputStream()));
                    answer = input.readLine();

                    System.out.println("\n"+answer+"\n");

                    if(answer != null){
                        JSONObject jsonMessage = new JSONObject(answer);
                        JSONArray jsonArray = jsonMessage.getJSONArray("Update_Cfm");
                        JSONObject object = jsonArray.getJSONObject(1);
                        json.set(object);
                    }
                    atomicBooleanMain.set(true);
                }

                //JSONArray jsonArray = jsonMessage.getJSONArray("Update_Cfm");
            }

            //String result = new String();

            //FriendsManager friendsManager = (FriendsManager) GlobalManager.getInstance().GetManager(ManagerEnum.FRIENDMANAGER);

            /*for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                if(object == null) {
                    System.out.println("ObjectNULL");
                    continue;
                }

                String tmp = object.getString("Id");

                int phoneNr = Integer.valueOf(tmp);

                System.out.println(phoneNr);

                Friend friend = friendsManager.getFriend(phoneNr);

                if(friend != null)
                {
                    friend.setmLatitude(Double.valueOf(object.getString("Lat")));
                    friend.setmLongitude(Double.valueOf(object.getString("Lon")));
                    friend.getmMarker().setPosition(new LatLng(friend.getmLatitude(),friend.getmLongitude()));
                    String status = object.getString("Vis");
                    friend.setmStatus(status.equals("0") ? VisibilityStatus.VISIBLE : status.equals("1") ? VisibilityStatus.INVISIBLE : VisibilityStatus.NOT_REGISTERED);
                    if(status.equals("0") && friend.getmMarker().isVisible() == false)
                        friend.getmMarker().setVisible(true);
                    else if(!status.equals("0"))
                        friend.getmMarker().setVisible(false);

                }

                result += object.getString("Id");
                result += " ";
                result += object.getString("Vis");
                result += " ";
                result += object.getString("Lat");
                result += " ";
                result += object.getString("Lon");
            }*/

            //System.out.println(result);

            //text.setText(result);
            //}

            //os.close();
            //input.close();


            /*DataInputStream is = null;
            is = new DataInputStream(s.getInputStream());
            answer = is.readLine();
            text.setText((CharSequence) answer);
            is.close();*/




        } catch (Exception e) {
            Log.d("error",e.toString());
        }
    }
}
