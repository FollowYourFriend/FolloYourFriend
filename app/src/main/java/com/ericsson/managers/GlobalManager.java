package com.ericsson.managers;

public class GlobalManager {

    private FriendsManager mFriendManager;

    private static final GlobalManager ourInstance = new GlobalManager();

    public static GlobalManager getInstance() {
        return ourInstance;
    }

    private GlobalManager() {
    }

    public boolean init() {
        mFriendManager = new FriendsManager();
        return true;
    }
    public  Manager GetManager(ManagerEnum manager) {
        Manager temp = null;
        switch (manager) {
            case FRIENDMANAGER:
                temp = mFriendManager;
        }
        return temp;
    }
}
