package com.example.todo_add_edit.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.RequestResult
import com.example.todo_add_edit.TodoAddEditInteractor
import com.example.todo_add_edit.models.TodoUI
import com.example.todo_utils.Priority
import com.example.todo_utils.convertLongToStringDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TodoAddEditViewModel @Inject constructor(
    val interactor: TodoAddEditInteractor,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    var state: State = State.Loading()
        private set

    var showDeleteDialog by mutableStateOf(false)
        private set

    private val _uiState: MutableStateFlow<TodoUI> = MutableStateFlow(TodoUI.empty)
    val uiState: StateFlow<TodoUI> = _uiState.asStateFlow()

    private var _dateDialogState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dateDialogState: StateFlow<Boolean> = _dateDialogState.asStateFlow()

    private var _switchState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val switchState: StateFlow<Boolean> = _switchState.asStateFlow()

    private var _toastMessageState: MutableStateFlow<String?> = MutableStateFlow(null)
    val toastMessageState: StateFlow<String?> = _toastMessageState.asStateFlow()

    init {
        val savedParam = savedStateHandle.get<String>("todoId")?.toInt()
        if (savedParam == -1) {
            val startUI = TodoUI(
                id = null,
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

                val result = interactor.getTodoById(savedParam)
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
        viewModelScope.launch {
            interactor.deleteTodo(uiState.value)
        }
    }

    fun updateDateDialogState(show: Boolean) {
        _dateDialogState.update { show }
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

    fun onDeadLineChange(deadline: Long) {
        _uiState.update {
            it.copy(deadLine = convertLongToStringDate(deadline))
        }
    }

    fun onToastMessageStateChange(toastText: String?) {
        _toastMessageState.update { toastText }
    }

    private suspend fun saveTodo() {
        if (uiState.value.text.isEmpty()) {
            onToastMessageStateChange("Нельзя сохранить пустой текст")
            return
        }

//        val id = uiState.value.id

//        if (id != null) {
//            val existingTodo = interactor.getTodoById(todoId = id)
//
//            if (existingTodo is RequestResult.Success && existingTodo.data.id != id) {
//                onToastMessageStateChange("Заметка с таким названием уже существует.")
//                return
//            }
//        }


        interactor.addTodo(uiState.value)
    }

    private suspend fun initUi(todoUI: TodoUI) {
        _uiState.emit(todoUI)
        updateSwitchState(todoUI.deadLine != null)
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
