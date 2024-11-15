package com.example.todo_add_edit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.domain.TodoInteractor
import com.example.todo_add_edit.models.TodoUI
import com.example.todo_add_edit.models.toTodo
import com.example.todo_utils.Priority
import com.example.todo_utils.convertLongToStringDateTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ExtendDeadlineReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var interactor: TodoInteractor

    override fun onReceive(context: Context?, intent: Intent) {
        val todoId = intent.getStringExtra("todoId")

        if (todoId != null) {
            context?.let {
                val notificationManager = NotificationManagerCompat.from(it)
                notificationManager.cancel(1)
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    withTimeout(3000L) {
                        val request = interactor.getTodoById(todoId)
                        val todo = request.data
                        todo?.let {
                            val newDeadlineMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                            val newDeadline = convertLongToStringDateTime(newDeadlineMillis)

                            interactor.updateDeadline(todoId, newDeadline)
                            rescheduleWork(it.toTodo(), newDeadlineMillis, newDeadline)
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    Log.d("Timeout", "Корутина превысила тайм-аут")
                }

            }
        }
    }

    private fun rescheduleWork(todoId: TodoUI, newDeadlineMillis: Long, newDeadline: String) {
        val delay = newDeadlineMillis - System.currentTimeMillis()
        val notificationWork = OneTimeWorkRequestBuilder<DeadlineNotificationWorker>()
            .setInitialDelay(
                if (delay < 0) 0 else newDeadlineMillis,
                TimeUnit.MILLISECONDS
            )
            .setInputData(
                workDataOf(
                    "todoId" to todoId.id,
                    "todoTitle" to todoId.text,
                    "todoDeadline" to newDeadline,
                    "priority" to when (todoId.priority) {
                        Priority.HIGH -> "высокая"
                        Priority.NORMAL -> "средняя"
                        Priority.LOW -> "низкая"
                    }
                )
            )
            .build()

        Log.d("rescheduleWork", "Reminder text ${todoId.text}, description $newDeadline")

        workManager.enqueueUniqueWork(
            todoId.text,
            ExistingWorkPolicy.REPLACE,
            notificationWork
        )
    }
}

