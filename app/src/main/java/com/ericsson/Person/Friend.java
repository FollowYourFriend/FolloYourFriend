package com.ericsson.Person;

import android.widget.EditText;

import com.google.android.gms.maps.model.Marker;

public class Friend implements PersonIf {
    private int mNumber;
    private String mName;
    private double mLongitude;
    private double mLatitude;
    private VisibilityStatus mStatus;
    private Marker mMarker = null;

    public Friend(int mNumber, String mName) {
        this.mNumber = mNumber;
        this.mName = mName;
    }

    public Friend(int phone, EditText name, EditText surrname) {
    }

    public int getmNumber() {
        return mNumber;
    }

    public void setmNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public VisibilityStatus getmStatus() {
        return mStatus;
    }

    public void setmStatus(VisibilityStatus mStatus) {
        this.mStatus = mStatus;
    }

    public Marker getmMarker() { return mMarker; }

    public void setmMarker(Marker mMarker) { this.mMarker = mMarker; }
}
