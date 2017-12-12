package com.exness.pushtest.services

import android.util.Log
import com.exness.pushtest.utils.DeviceUtils

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by alexander.shtanko on 12/12/17.
 */

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    val TAG = MyFirebaseInstanceIDService::class.java.simpleName

    override fun onTokenRefresh() {
        try {
            val refreshedToken = FirebaseInstanceId.getInstance().token
            val mDatabase = FirebaseDatabase.getInstance().reference
            if(refreshedToken!=null)
            mDatabase.child("tokens").child(DeviceUtils.getDeviceId(this)).setValue(refreshedToken)
        } catch (e: Exception) {
            Log.e(TAG, "", e)
        }
    }


}
