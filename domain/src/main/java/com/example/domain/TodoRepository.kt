package com.example.domain

import com.example.domain.model.RequestResult
import com.example.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getAll(): Flow<RequestResult<List<Todo>>>

    fun getAllFromDatabase(): Flow<RequestResult<List<Todo>>>

    suspend fun getTodoById(id: String): RequestResult<Todo>

    suspend fun updateDeadline(id: String, newDeadline: String)

    suspend fun insertTodoToLocalBD(todo: Todo)

    suspend fun deleteTodoToLocalBD(todo: Todo)
}