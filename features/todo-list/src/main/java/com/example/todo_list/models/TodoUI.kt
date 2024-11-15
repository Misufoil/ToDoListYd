package com.example.todo_list.models

import androidx.compose.runtime.Immutable
import com.example.domain.model.Todo
import com.example.todo_utils.Priority

@Immutable
data class TodoUI(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val priority: Priority,
    val creationDate: String,
    val refactorData: String?,
    val deadLine: String?,
)