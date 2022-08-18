package com.common.cmnpop.util;


import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressLint("InlinedApi")
public final class PermissionConstants {

    public static final String STORAGE = "STORAGE";

    private static final String[] GROUP_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @StringDef({STORAGE,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionGroup {
    }

    public static String[] getPermissions(@PermissionGroup final String permission) {
        if (permission == null) return new String[0];
        if (STORAGE.equals(permission)) {
            return GROUP_STORAGE;
        }
        return new String[]{permission};
    }
}