package com.example.todolistyd.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.example.database.TodoDatabase
import com.example.database.TodoRepositoryImpl
import com.example.database.todoDatabase
import com.example.domain.TodoRepository
import com.example.main_activity_api.MainActivityIntentRouter
import com.example.main_activity_impl.MainActivityIntentRouterImpl
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
    fun bindMainActivity(activity: MainActivityIntentRouterImpl): MainActivityIntentRouter

    @Binds
    fun bindTodoRepository(repositoryImpl: TodoRepositoryImpl): TodoRepository

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
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val channel = NotificationChannel(
                "todo_channel",
                "Main Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManagerCompat.createNotificationChannel(channel)
            return notificationManagerCompat
        }

        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}