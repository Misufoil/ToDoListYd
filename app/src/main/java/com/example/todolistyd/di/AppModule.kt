package com.example.todolistyd.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.example.database.TodoDatabase
import com.example.database.todoDatabase
import com.example.main_activity_api.MainActivityLauncher
import com.example.main_activity_impl.MainActivityLauncherImpl
import com.example.todo_add_edit.TodoAddEditInteractor
import com.example.todo_add_edit.TodoAddEditInteractorImpl
import com.example.todo_list.TodoListInteractor
import com.example.todo_list.TodoListInteractorImpl
import com.example.todo_utils.AppDispatchers
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindMainActivity(activity: MainActivityLauncherImpl): MainActivityLauncher

    @Binds
    fun bindTodoListInteractor(interactor: TodoListInteractorImpl): TodoListInteractor

    @Binds
    fun bindTodoAddEditInteractor(interactor: TodoAddEditInteractorImpl): TodoAddEditInteractor

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
            return todoDatabase(context)
        }

        @Provides
        @Singleton
        fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

        @Singleton
        @Provides
        fun provideNotificationManager(
            @ApplicationContext context: Context
        ): NotificationManagerCompat {
            val notificationManager = NotificationManagerCompat.from(context)
            val channel = NotificationChannel(
                "todo_channel",
                "Main Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            return notificationManager
        }

        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}