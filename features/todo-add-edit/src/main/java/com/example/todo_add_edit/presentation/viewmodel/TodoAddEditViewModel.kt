package com.example.todo_add_edit.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.domain.TodoInteractor
import com.example.domain.model.RequestResult
import com.example.domain.model.map
import com.example.todo_add_edit.DeadlineNotificationWorker
import com.example.todo_add_edit.models.TodoUI
import com.example.todo_add_edit.models.toTodo
import com.example.todo_utils.Priority
import com.example.todo_utils.combineDateAndTime
import com.example.todo_utils.convertStringToDateTimeLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
internal class TodoAddEditViewModel @Inject constructor(
    private val interactor: TodoInteractor,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state: State = State.Loading()

    var showDeleteDialog by mutableStateOf(false)

    private val _uiState: MutableStateFlow<TodoUI> = MutableStateFlow(TodoUI.empty)
    val uiState: StateFlow<TodoUI> = _uiState.asStateFlow()

    private var _dateDialogState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dateDialogState: StateFlow<Boolean> = _dateDialogState.asStateFlow()

    private var _timeDialogState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val timeDialogState: StateFlow<Boolean> = _timeDialogState.asStateFlow()

    private var _switchState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val switchState: StateFlow<Boolean> = _switchState.asStateFlow()

    private var _toastMessageState: MutableStateFlow<String?> = MutableStateFlow(null)
    val toastMessageState: StateFlow<String?> = _toastMessageState.asStateFlow()

    private var _selectedDate: MutableStateFlow<Long?> = MutableStateFlow(null)

    init {
        val savedParam = savedStateHandle.get<String>("todoId")
        if (savedParam == "Add") {
            val startUI = TodoUI(
                id = "",
                text = "",
                isDone = false,
                priority = Priority.NORMAL,
                creationDate = "",
                refactorData = "",
                deadLine = null,
            )

            _uiState.update { startUI }
            state = State.Success(startUI)

        } else if (savedParam != null) {
            viewModelScope.launch {
                state = State.Loading()

                val result = interactor.getTodoById(savedParam).map { it.toTodo() }
                state = result.toState()

                if (result is RequestResult.Success) {
                    initUi(result.data)
                } else {
                    println("Error fetching data: $result")
                }
            }
        } else {
            state = State.Error()
        }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            saveTodo()
        }
    }

    fun changeDeleteDialogState(state: Boolean) {
        showDeleteDialog = state
    }

    fun deleteTodo() {
        deleteWorker(uiState.value.id)
        viewModelScope.launch {
            interactor.deleteTodo(uiState.value.toTodo())
        }
    }

    fun updateDateDialogState(show: Boolean) {
        _dateDialogState.update { show }
    }

    fun updateTimeDialogState(show: Boolean) {
        _timeDialogState.update { show }
    }

    fun updateSwitchState(state: Boolean) {
        if (!state) {
            _uiState.update {
                it.copy(deadLine = null)
            }
        }
        _switchState.update { state }
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(text = text)
        }
    }

    fun onPriorityChange(priority: Priority) {
        _uiState.update {
            it.copy(priority = priority)
        }
    }

    fun onToastMessageStateChange(toastText: String?) {
        _toastMessageState.update { toastText }
    }

    fun onDateSelected(date: Long) {
        _selectedDate.update { date }
        updateTimeDialogState(true)
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        _selectedDate.value?.let { date ->
            val deadline = combineDateAndTime(date, hour, minute)
            onDeadLineChange(deadline)
        }
        updateTimeDialogState(false)
    }

    private fun deleteWorker(id: String) {
        val result = workManager.cancelUniqueWork(id).result
        Log.d("deleteWorker", result.toString())
    }

    private fun onDeadLineChange(deadline: String) {
        _uiState.update {
            it.copy(deadLine = deadline)
        }
    }

    private suspend fun saveTodo() {
        if (uiState.value.text.isEmpty()) {
            onToastMessageStateChange("Нельзя сохранить пустой текст")
            return
        }
        if (uiState.value.id.isEmpty()) {
            _uiState.update {
                it.copy(id = UUID.randomUUID().toString())
            }
        }

        interactor.addTodo(uiState.value.toTodo())

        if (uiState.value.deadLine != null) {
            scheduleTodoNotification()
        } else {
            deleteWorker(id = uiState.value.id)
        }
    }

    private suspend fun initUi(todoUI: TodoUI) {
        _uiState.emit(todoUI)
        updateSwitchState(todoUI.deadLine != null)
    }

    private fun scheduleTodoNotification() {
        val notificationWork = OneTimeWorkRequestBuilder<DeadlineNotificationWorker>()
            .setInitialDelay(
                convertStringToDateTimeLong(uiState.value.deadLine!!) - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
            )
            .setInputData(
                workDataOf(
                    "todoId" to uiState.value.id,
                    "todoTitle" to uiState.value.text,
                    "todoDeadline" to uiState.value.deadLine,
                    "priority" to when (uiState.value.priority) {
                        Priority.HIGH -> "высокая"
                        Priority.NORMAL -> "средняя"
                        Priority.LOW -> "низкая"
                    }
                )
            )
            .build()

        workManager.enqueueUniqueWork(
            uiState.value.id,
            ExistingWorkPolicy.REPLACE,
            notificationWork
        )
    }
}

internal sealed class State(open val data: TodoUI?) {
    data object None : State(data = null)
    class Loading(data: TodoUI? = null) : State(data = data)
    class Error(data: TodoUI? = null) : State(data = data)
    class Success(override val data: TodoUI) : State(data = data)
}

private fun RequestResult<TodoUI>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Loading(data)
        is RequestResult.InProgress -> State.Error(data)
        is RequestResult.Success -> State.Success(data)
    }
}
