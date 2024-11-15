package com.example.database

import com.example.database.models.TodoDBO
import com.example.database.models.toTodo
import com.example.domain.model.RequestResult
import com.example.domain.model.Todo
import com.example.domain.model.map
import com.example.database.models.toTodoDBO
import com.example.domain.TodoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class TodoRepositoryImpl @Inject constructor(
    private val database: TodoDatabase
): TodoRepository {

    override fun getAll(): Flow<RequestResult<List<Todo>>> {
        val localData = getAllFromDatabase()

        return localData
    }

    override fun getAllFromDatabase(): Flow<RequestResult<List<Todo>>> {
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

    override suspend fun getTodoById(id: String): RequestResult<Todo> {
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

    override suspend fun updateDeadline(id: String, newDeadline: String) {
        database.todoDao.updateDeadline(id, newDeadline)
    }

    override suspend fun insertTodoToLocalBD(todo: Todo) {
        database.todoDao.insert(todo.toTodoDBO())
    }

    override suspend fun deleteTodoToLocalBD(todo: Todo) {
        database.todoDao.delete(todo.toTodoDBO())
    }
}