package com.example.database

import androidx.room.TypeConverter
import com.example.todo_utils.Priority

internal class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return  priority.name
    }

    @TypeConverter
    fun toPriority(string: String): Priority  {
        return Priority.valueOf(string)
    }
}