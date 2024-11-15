package com.example.todo_add_edit.models

import com.example.domain.model.Todo


internal fun Todo.toTodo(): TodoUI {
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
