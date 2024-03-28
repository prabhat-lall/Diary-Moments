package com.example.mynotes

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val context = applicationContext
        val channelId = "DailyNotifications"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily notifications"
                enableLights(true)
                lightColor = Color.BLUE
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Correct permission check for notifications


        try {
            val notificationIntent = Intent(context, SplashScreen::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE here
            )

            val notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle("Daily Notification")
                .setContentText("This is your daily notification.")
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }else{
                NotificationManagerCompat.from(context).notify(1, notification)
            }

        }catch (e:Exception){
            Log.e("_prabhat", "showNotification: ${e.toString()}", )
        }


    }

}