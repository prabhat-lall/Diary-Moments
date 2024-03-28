package com.example.mynotes

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val builder = NotificationCompat.Builder(context,"NotifyNotification")
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentText("My Diary")
            .setContentText("Your Diary is calling you. Need Your Help")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationManager.notify(200,builder.build())
    }
}