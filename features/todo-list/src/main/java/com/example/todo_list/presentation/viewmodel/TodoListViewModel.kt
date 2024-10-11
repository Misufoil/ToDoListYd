package com.example.todo_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.domain.model.RequestResult
import com.example.todo_list.TodoListInteractor
import com.example.todo_list.models.TodoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TodoListViewModel @Inject constructor(
    private val interactor: TodoListInteractor,
    private val workManager: WorkManager
) : ViewModel() {

    val state: StateFlow<State> = interactor.getAllTodo()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    private var lastDeletedTodo: TodoUI? = null

    val completedTasks: StateFlow<Int> = state
        .map { state ->
            state.todos?.count { it.isDone } ?: 0
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun completeTodo(todoUI: TodoUI) {
        viewModelScope.launch {
            interactor.insertTodo(todoUI.copy(isDone = true))
        }
    }

    fun deleteTodo(todoUI: TodoUI) {
        viewModelScope.launch {
            interactor.deleteTodo(todoUI)
            lastDeletedTodo = todoUI
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            lastDeletedTodo?.let {
                interactor.insertTodo(it)
            }
            lastDeletedTodo = null
        }
    }

    fun deleteWorker(todo: TodoUI) {
        workManager.cancelUniqueWork(todo.text)
    }
}

internal sealed class State(open val todos: List<TodoUI>?) {
    data object None : State(todos = null)
    class Loading(todos: List<TodoUI>? = null) : State(todos)
    class Error(todos: List<TodoUI>? = null) : State(todos)
    class Success(override val todos: List<TodoUI>) : State(todos)
}

private fun RequestResult<List<TodoUI>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(data)
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
}