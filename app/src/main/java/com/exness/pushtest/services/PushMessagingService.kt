package com.exness.pushtest.services

import android.content.Intent
import android.util.Log

import com.exness.pushtest.BuildConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by alexander.shtanko on 12/12/17.
 */

class PushMessagingService : FirebaseMessagingService() {
    private val TAG = PushMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        try {
            Log.d(TAG, "From: " + remoteMessage!!.from!!)
            startService(Intent(this, QuotesService::class.java))
        } catch (e: Exception) {
            Log.e(TAG, "", e)
        }


    }

}
