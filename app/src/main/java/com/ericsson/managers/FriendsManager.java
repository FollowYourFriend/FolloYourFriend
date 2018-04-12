package com.ericsson.managers;

import com.ericsson.Person.Friend;

import java.util.ArrayList;

public class FriendsManager implements Manager {

    private ArrayList<Friend> mFriends;

    public ArrayList<Friend> getmFriends() {
        return mFriends;
    }

    public void setmFriends(ArrayList<Friend> mFriends) {
        this.mFriends = mFriends;
    }

    public void addmFriends(Friend Friend) {
        this.mFriends.add(Friend);
    }
}
