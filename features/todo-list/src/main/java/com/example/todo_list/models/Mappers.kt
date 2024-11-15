package com.example.todo_list.models

import com.example.domain.model.Todo

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
