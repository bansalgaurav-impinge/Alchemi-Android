package com.app.alchemi.utils.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log


class OnClearFromRecentService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("ClearFromRecentService", "Service Started")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //Config.setNotificationManager("END")
        Log.d("ClearFromRecentService", "Service Destroyed")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
       // Config.setNotificationManager("END")
        Log.e("ClearFromRecentService", "END")

        stopSelf()
    }
}