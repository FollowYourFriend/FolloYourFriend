package com.ericsson.managers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionManager implements Manager {

    Activity mActivity = null;

    public void requestForPermission(String[] permissions, Activity activity)
    {
        mActivity = activity;

        for(String permission : permissions) {
            if (!isPermissionGranted(permission)) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{permission},
                        1);
            }
        }
    }

    public boolean isPermissionGranted(String permission)
    {
        return ContextCompat.checkSelfPermission(mActivity,
                permission) == PERMISSION_GRANTED;
    }

    private boolean checkResult(int[] grantResults)
    {
        for(int result: grantResults)
        {
            if(result != PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

}
