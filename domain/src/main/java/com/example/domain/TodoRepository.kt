package com.example.domain

import com.example.database.TodoDatabase
import com.example.database.models.TodoDBO
import com.example.domain.model.RequestResult
import com.example.domain.model.Todo
import com.example.domain.model.map
import com.example.domain.model.toTodo
import com.example.domain.model.toTodoDBO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class TodoRepository @Inject constructor(
    private val database: TodoDatabase
) {

    fun getAll(): Flow<RequestResult<List<Todo>>> {
        val localData = getAllFromDatabase()

        return localData
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<Todo>>> {
        val dbRequestResult = database.todoDao
            .observeAll()
            .map<List<TodoDBO>, RequestResult<List<TodoDBO>>> { RequestResult.Success(it) }
            .catch { emit(RequestResult.Error(error = it)) }

        val start = flowOf<RequestResult<List<TodoDBO>>>(RequestResult.InProgress())

        return merge(dbRequestResult, start)
            .map { result ->
                result.map { todoDBO ->
                    todoDBO.map { it.toTodo() }
                }
            }
    }

    suspend fun getTodoById(id: String): RequestResult<Todo> {
        return try {
            val requestResult = database.todoDao.getById(id)
            if (requestResult != null) {
                RequestResult.Success(requestResult.toTodo())
            } else {
                RequestResult.Error(null, NoSuchElementException("Todo not found"))
            }
        } catch (e: Exception) {
            RequestResult.Error(null, e)
        }

    }

    suspend fun insertTodoToLocalBD(todo: Todo) {
        database.todoDao.insert(todo.toTodoDBO())
    }

    suspend fun deleteTodoToLocalBD(todo: Todo) {
        database.todoDao.delete(todo.toTodoDBO())
    }
}