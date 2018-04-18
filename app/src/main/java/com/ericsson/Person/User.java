package com.ericsson.Person;

import android.location.LocationManager;
import android.telephony.TelephonyManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class User implements PersonIf {

    private Marker mMarker = null;
    private String mName;
    private String mPhoneNr = "";

    private boolean isLocationValid = false;

    private static final User ourInstance = new User();

    public static User getInstance() { return ourInstance; }

    private User() {}

    public boolean init() {
        mName = "Ja";

        return true;
    }

    public Marker getmMarker() { return mMarker; }

    public void setmMarker(Marker marker) { mMarker = marker; }

    public String getmName() { return mName; }

    public void setmName(String name) { mName = name; }

    public String getmPhoneNr() { return mPhoneNr; }

    public void setmPhoneNr(String phoneNr) { mPhoneNr = phoneNr; }


    public void setLocationValid(boolean locationValid) {
        isLocationValid = locationValid;
    }

    public boolean isLocationValid() {
        return isLocationValid;
    }
}
