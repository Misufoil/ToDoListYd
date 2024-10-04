package com.example.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo_utils.Priority

@Entity(tableName = "TodoDBO")
data class TodoDBO (
    @PrimaryKey val id: Int?,
    @ColumnInfo val text: String,
    @ColumnInfo val isDone: Boolean,
    @ColumnInfo val priority: Priority,
    @ColumnInfo val creationDate: String,
    @ColumnInfo val refactorData: String?,
    @ColumnInfo val deadLine: String?,
)