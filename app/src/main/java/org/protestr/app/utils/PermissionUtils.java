package org.protestr.app.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;

/**
 * Created by someone on 25/08/17.
 */

public class PermissionUtils {
    public static boolean isPermissionGranted(@NonNull Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
