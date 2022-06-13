/*
 * *
 *  * Created by Ali Ashkeyev on 20.05.2022, 9:25
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 20.05.2022, 9:25
 *
 */

package com.example.budka.fcm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.budka.R
import com.example.budka.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.fcm.MessagingService
import android.app.NotificationChannel
import android.graphics.Color
import android.os.Build


class BudkaFireBaseMessagingService: MessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        sendNotification(data)

    }

    @SuppressLint("WrongConstant")
    private fun sendNotification(data: MutableMap<String, String>){
        val title = data["title"]
        val message = data["body"]
        val deepLink = data["deepLink"]
        val args = Bundle()
        args.putString("deepLink", deepLink)
        val foregroundArgs = Bundle()
        deepLink?.let {
            it.substring(it.lastIndexOf('/') + 1).toInt()
        }?.let { foregroundArgs.putInt("petId", it) }


        val deeplinkBuilder= NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.petDetailFragment)
            .setArguments(foregroundArgs)
            .createPendingIntent()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_MAX
            )

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val builder = NotificationCompat.Builder(
            this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setAutoCancel(true)
            .setContentIntent(deeplinkBuilder)


//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
        notificationManager.notify(1, builder.build())
//
    }
}