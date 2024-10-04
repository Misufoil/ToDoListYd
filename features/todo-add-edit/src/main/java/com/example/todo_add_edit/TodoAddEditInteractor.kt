package com.example.todo_add_edit

import com.example.domain.TodoRepository
import com.example.domain.model.RequestResult
import com.example.domain.model.map
import com.example.todo_add_edit.models.TodoUI
import com.example.todo_add_edit.models.toTodo
import javax.inject.Inject

interface TodoAddEditInteractor {
    suspend fun getTodoById(todoId: Int): RequestResult<TodoUI>
    suspend fun addTodo(todoUI: TodoUI)
    suspend fun deleteTodo(todoUI: TodoUI)
}

class TodoAddEditInteractorImpl @Inject constructor(val repository: TodoRepository): TodoAddEditInteractor {
    override suspend fun getTodoById(todoId: Int): RequestResult<TodoUI>  {
        return repository.getTodoById(todoId).map { it.toTodo() }
    }

    override suspend fun addTodo(todoUI: TodoUI) {
        repository.insertTodoToLocalBD(todoUI.toTodo())
    }

    override suspend fun deleteTodo(todoUI: TodoUI) {
        repository.deleteTodoToLocalBD(todoUI.toTodo())
    }
}