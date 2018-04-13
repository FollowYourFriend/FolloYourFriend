package com.ericsson.managers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager implements Manager {

    public void requestForPermission(String[] permissions, Activity activity)
    {
        for(String permission : permissions) {
            if (!isPermissionGranted(permission, activity)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        1);
            }
        }
    }

    public void checkIfAllNeededPermissionsAreGranted(String[] mandatoryPermissions, Activity activity)
    {
        for(String permission : mandatoryPermissions)
        {
            if(!isPermissionGranted(permission,activity))
                System.exit(0);
        }
    }

    public boolean isPermissionGranted(String permission, Activity activity)
    {
        return ContextCompat.checkSelfPermission(activity,
                permission) == PackageManager.PERMISSION_GRANTED;
    }
}
