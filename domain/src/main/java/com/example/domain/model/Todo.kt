package com.example.domain.model

import com.example.todo_utils.Priority

data class Todo(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val priority: Priority,
    val creationDate: String,
    val refactorData: String?,
    val deadLine: String?,
)