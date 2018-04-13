package com.ericsson.Person;

import android.widget.EditText;

public class Friend implements PersonIf {
    private int mNumber;
    private String mName;
    private double mLongitude;
    private double mLatitude;
    private VisibilityStatus mStatus;

    public Friend(int mNumber, String mNamee) {
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
}
