
package com.rovaind.academy.Utils.exoplayerview.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    public static final int PERMISSION_STORAGE_TAG = 255;
    public static final int PERMISSION_SETTINGS_TAG = 254;


    public static final int PERMISSION_SYSTEM_RINGTONE = 42;
    public static final int PERMISSION_SYSTEM_BRIGHTNESS = 43;
    public static final int PERMISSION_SYSTEM_DRAW_OVRLAYS = 44;

    /*
     * Marshmallow permission system management
     */

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean canDrawOverlays(Context context) {
        return !AndroidUtil.isMarshMallowOrLater() || Settings.canDrawOverlays(context);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean canWriteSettings(Context context) {
        return !AndroidUtil.isMarshMallowOrLater() || Settings.System.canWrite(context);
    }

    public static boolean canReadStorage(Context context) {
        return !AndroidUtil.isMarshMallowOrLater() || ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    private static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_STORAGE_TAG);
    }
}
