package com.ericsson.managers;

public class GlobalManager {

    private FriendsManager mFriendManager;
    private PermissionManager mPermissionManager;

    private static final GlobalManager ourInstance = new GlobalManager();

    public static GlobalManager getInstance() {
        return ourInstance;
    }

    private GlobalManager() {
    }

    public boolean init() {
        mFriendManager = new FriendsManager();
        mPermissionManager = new PermissionManager();
        return true;
    }
    public  Manager GetManager(ManagerEnum manager) {
        Manager temp = null;
        switch (manager) {
            case FRIENDMANAGER:
                temp = mFriendManager;
            case PERMISSIONMANAGER:
                temp = mPermissionManager;
        }
        return temp;
    }
}
