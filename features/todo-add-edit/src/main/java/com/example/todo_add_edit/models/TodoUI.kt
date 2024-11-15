package com.example.todo_add_edit.models

import androidx.compose.runtime.Immutable
import com.example.domain.model.Todo
import com.example.todo_utils.Priority
import java.util.UUID

@Immutable
data class TodoUI(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val priority: Priority,
    val creationDate: String,
    val refactorData: String?,
    val deadLine: String?,
) {
    companion object {
        val empty = TodoUI(
            id = "",
            text = "",
            isDone = false,
            priority = Priority.NORMAL,
            creationDate = "",
            refactorData = "",
            deadLine = "",
        )
    }
}