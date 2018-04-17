package com.ericsson.Person;

import com.google.android.gms.maps.model.Marker;

public class User implements PersonIf {

    private Marker mMarker;

    private static final User ourInstance = new User();

    public static User getInstance() { return ourInstance; }

    public boolean init() {


        return true;
    }

    public Marker getmMarker() { return mMarker; }

    public void setmMarker(Marker marker) { mMarker = marker; }

}
