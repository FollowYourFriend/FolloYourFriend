package com.ericsson.managers;

import com.ericsson.Person.Friend;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class FriendsManager implements Manager {

    private ArrayList<Friend> mFriends;

    public FriendsManager() {
        mFriends = new ArrayList<>();
    }

    public ArrayList<Friend> getFriends() {
        return mFriends;
    }

    public void addmFriends(Friend Frienda) {
        this.mFriends.add(Frienda);
    }

    public boolean checkIfFriendNumberIsAlreadyOnList(Friend FriendB) {
        boolean isOnList = false;
        for(Friend temp : mFriends){
            if(FriendB.getmNumber() == temp.getmNumber()) {
                isOnList = true;
            }
        }
        return isOnList;
    }

    public Friend getFriend(int phoneNr) {
        for(Friend friend: mFriends) {
            if(friend.getmNumber() == phoneNr)
                return friend;
        }

        return null;
    }

    public void setFriends(ArrayList<Friend> list){
        mFriends = list;
    }

}
