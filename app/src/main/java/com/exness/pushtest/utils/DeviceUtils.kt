package com.exness.pushtest.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

import com.exness.pushtest.services.MyFirebaseInstanceIDService

/**
 * Created by alexander.shtanko on 12/12/17.
 */

object DeviceUtils {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID)
    }
}
