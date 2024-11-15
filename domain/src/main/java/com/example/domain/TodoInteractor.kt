package com.example.domain

import com.example.domain.model.RequestResult
import com.example.domain.model.Todo
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class TodoInteractor @Inject constructor(val repository: TodoRepository) {

    fun getAllTodo(): Flow<RequestResult<List<Todo>>> {
        return repository.getAll()
    }

    suspend fun getTodoById(todo: String): RequestResult<Todo> {
        return repository.getTodoById(todo)
    }

    suspend fun addTodo(todo: Todo) {
        repository.insertTodoToLocalBD(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        repository.deleteTodoToLocalBD(todo)
    }

    suspend fun insertTodo(todo: Todo) {
        repository.insertTodoToLocalBD(todo)
    }

    suspend fun updateDeadline(id: String, newDeadline: String) {
        repository.updateDeadline(id, newDeadline)
    }
}