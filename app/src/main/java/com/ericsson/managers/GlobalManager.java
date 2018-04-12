package com.ericsson.managers;

public class GlobalManager {

    static FriendsManager mFriendManager;

    private static final GlobalManager ourInstance = new GlobalManager();

    public static GlobalManager getInstance() {
        return ourInstance;
    }

    private GlobalManager() {
    }

    public static void Init() {
        mFriendManager = new FriendsManager();
    }

    public static Manager GetManager(ManagerEnum manager) {
        Manager temp = null;
        switch (manager) {
            case FRIENDMANAGER:
                temp = mFriendManager;
        }
        return temp;
    }
}
