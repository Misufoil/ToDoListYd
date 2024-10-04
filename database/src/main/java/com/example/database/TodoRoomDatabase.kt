package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.dao.TodoDao
import com.example.database.models.TodoDBO

@Database(entities = [TodoDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class TodoRoomDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao
}

class TodoDatabase internal constructor(private val database: TodoRoomDatabase) {
    val todoDao: TodoDao
        get() = database.todoDao()
}

fun todoDatabase(applicationContext: Context): TodoDatabase {
    val todoRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        TodoRoomDatabase::class.java,
        "todos"
    ).build()

    return TodoDatabase(todoRoomDatabase)
}