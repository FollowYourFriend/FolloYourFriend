package com.ericsson.Person;

import android.location.LocationManager;
import android.telephony.TelephonyManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static com.ericsson.Person.VisibilityStatus.INVISIBLE;

public class User implements PersonIf {

    private Marker mMarker = null;
    private String mName;
    private String mPhoneNr = "";
    private String mVisibility = "0";
    private double mLatitude = 0;
    private double mLongitude = 0;

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

    public String getmVisibility() {
        return mVisibility;
    }

    public void setmVisibility(VisibilityStatus status) {
        switch (status) {
            case VISIBLE:
                mVisibility = "0";
                break;
            case INVISIBLE:
                mVisibility = "1";
                break;
            case NOT_REGISTERED:
                mVisibility = "2";
                break;
        }
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
