package com.dj.insulink.core.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

class ReminderReceiver() : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Reminder"
        val message = intent.getStringExtra("message") ?: "You have a reminder!"
        val notificationId = intent.getIntExtra("notificationId", 0)

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification(title, message, notificationId)
    }
}