package com.example.domain.model

import com.example.database.models.TodoDBO

internal fun TodoDBO.toTodo(): Todo {
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

internal fun Todo.toTodoDBO(): TodoDBO {
    return TodoDBO(
        id = id,
        text = text,
        isDone = isDone,
        priority = priority,
        creationDate = creationDate,
        refactorData = refactorData,
        deadLine = deadLine
    )
}