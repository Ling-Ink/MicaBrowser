package com.moling.micabrowser.utils;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ProgramUtils {
    public static String getAppVersion() {
        try {
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
