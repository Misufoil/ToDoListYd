package com.example.todolistyd.di

import android.content.Context
import com.example.database.TodoDatabase
import com.example.database.todoDatabase
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
    }
}