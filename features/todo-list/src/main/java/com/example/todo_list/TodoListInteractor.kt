package com.example.todo_list

import com.example.domain.TodoRepository
import com.example.domain.model.RequestResult
import com.example.domain.model.map
import com.example.todo_list.models.TodoUI
import com.example.todo_list.models.toTodo
import com.example.todo_list.models.toTodoUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TodoListInteractor {
    fun getAllTodo(): Flow<RequestResult<List<TodoUI>>>
    suspend fun deleteTodo(todoUI: TodoUI)
    suspend fun insertTodo(todoUI: TodoUI)
}

class TodoListInteractorImpl @Inject constructor(private val repository: TodoRepository): TodoListInteractor {
    override fun getAllTodo(): Flow<RequestResult<List<TodoUI>>> {
        return repository.getAll().map { requestResult ->
            requestResult.map { todos ->
                todos.map { it.toTodoUI() }
            }
        }
    }

    override suspend fun deleteTodo(todoUI: TodoUI) {
        repository.deleteTodoToLocalBD(todoUI.toTodo())
    }

    override suspend fun insertTodo(todoUI: TodoUI) {
        repository.insertTodoToLocalBD(todoUI.toTodo())
    }
}

