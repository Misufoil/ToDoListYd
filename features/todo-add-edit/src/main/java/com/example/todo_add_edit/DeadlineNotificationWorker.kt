package com.example.todo_add_edit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.example.app_uikit.R as uikitR

@HiltWorker
class DeadlineNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationManager: NotificationManagerCompat
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val todoTitle = inputData.getString("todoTitle")
        val todoDeadline = inputData.getString("todoDeadline")
        val priority = inputData.getString("priority")

        return try {
            sendNotification(todoTitle, todoDeadline, priority)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }


    private fun sendNotification(todoTitle: String?, todoDeadline: String?, priority: String?) {
        Log.d("sendNotification", "Reminder name $todoTitle, description $todoDeadline")


        val notification = NotificationCompat.Builder(applicationContext, "todo_channel")
            .setContentTitle(todoTitle)
            .setContentText("$todoDeadline\nВажность: $priority")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(uikitR.drawable.baseline_today_24)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("aaaaa", "Reminder name , description $todoDeadline")
        }

        notificationManager.notify(1, notification.build())
        Log.d("ReminderWorker", "Reminder name $todoTitle, description $todoDeadline")
        return
    }
}