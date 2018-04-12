package com.ericsson.managers;

import com.ericsson.Person.Friend;

import java.util.ArrayList;

public class FriendsManager implements Manager {

    private ArrayList<Friend> mFriends;

    public FriendsManager() {
        mFriends = new ArrayList<>();
    }

    public ArrayList<Friend> getmFriends() {
        return mFriends;
    }

    public void addmFriends(Friend Frienda) {
        System.out.println("kopytko222");
        this.mFriends.add(Frienda);
        System.out.println("kopytko221");
    }
}
