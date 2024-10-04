package com.example.todo_list.models

import androidx.compose.runtime.Immutable
import com.example.domain.model.Todo
import com.example.todo_utils.Priority
import kotlin.random.Random

@Immutable
data class TodoUI(
    val id: Int?,
    val text: String,
    val isDone: Boolean,
    val priority: Priority,
    val creationDate: String,
    val refactorData: String?,
    val deadLine: String?,
) {
    companion object {
        val empty = TodoUI(
            id = null,
            text = "",
            isDone = false,
            priority = Priority.NORMAL,
            creationDate = "",
            refactorData = null,
            deadLine = null,
        )
    }
}

internal fun Todo.toTodoUI(): TodoUI {
    return TodoUI(
        id = id,
        text = text,
        isDone = isDone,
        priority = priority,
        creationDate = creationDate,
        refactorData = refactorData,
        deadLine = deadLine
    )
}
internal fun TodoUI.toTodo(): Todo {
    return Todo(
        id = id,
        text = text,
        isDone = isDone,
        priority = priority,
        creationDate = creationDate,
        refactorData = refactorData,
        deadLine = deadLine
    )
}