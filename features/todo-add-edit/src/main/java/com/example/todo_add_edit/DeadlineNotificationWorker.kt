package com.example.todo_add_edit

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.main_activity_api.MainActivityIntentRouter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.example.app_uikit.R as uikitR

@HiltWorker
class DeadlineNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationManager: NotificationManagerCompat,
    private val mainActivityIntentRouter: MainActivityIntentRouter
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val todoId = inputData.getString("todoId")
        val todoTitle = inputData.getString("todoTitle")
        val todoDeadline = inputData.getString("todoDeadline")
        val priority = inputData.getString("priority")

        return try {
            sendNotification(todoId, todoTitle, todoDeadline, priority)
            Result.success()
        } catch (e: Exception) {
            e.message?.let { Log.d("Error", it) }
            Result.failure()
        }
    }

    private fun sendNotification(todoId: String?,todoTitle: String?, todoDeadline: String?, priority: String?) {
        Log.d("sendNotification", "Reminder name $todoTitle, description $todoDeadline")


        val intent = todoId?.let { mainActivityIntentRouter.launch(applicationContext, it) }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(applicationContext, "todo_channel")
            .setContentTitle(todoTitle).setContentText("$todoDeadline\nВажность: $priority")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(uikitR.drawable.baseline_today_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("POST_NOTIFICATIONS", "Reminder name , description $todoDeadline")
        }

        notificationManager.notify(1, notification.build())
        Log.d("ReminderWorker", "Reminder name $todoTitle, description $todoDeadline")
        return
    }
}