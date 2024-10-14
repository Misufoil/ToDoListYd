package com.example.todo_add_edit.presentation.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todo_add_edit.models.TodoUI
import com.example.todo_add_edit.presentation.viewmodel.State
import com.example.todo_add_edit.presentation.viewmodel.TodoAddEditViewModel
import com.example.todo_utils.Priority
import com.example.todo_utils.getCurrentTimeString
import com.example.app_uikit.R as uikitR

@Composable
fun TodoAddEditFeature(
    onPopBackStack: () -> Unit,
    padding: PaddingValues,
) {
    val viewModel: TodoAddEditViewModel = hiltViewModel()
    val currentState = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                onSaveButtonClick = { viewModel.onSaveButtonClick() },
                onPopBackStack = { onPopBackStack() }
            )
        },
        containerColor = ToDoListTheme.customColorsPalette.back_primary
    ) { padding ->
        when (currentState) {
            is State.None -> Unit
            is State.Error -> ErrorMessage(currentState, padding)
            is State.Loading -> ProgressIndicator(currentState, padding)
            is State.Success -> {
                TodoScreen(
                    padding = padding,
                    toastMessageState = viewModel.toastMessageState.collectAsState().value,
                    todoUiState = viewModel.uiState.collectAsState().value,
                    dateDialogState = viewModel.dateDialogState.collectAsState().value,
                    timeDialogState = viewModel.timeDialogState.collectAsState().value,
                    switchState = viewModel.switchState.collectAsState().value,
                    updateDateDialogState = { show: Boolean ->
                        viewModel.updateDateDialogState(
                            show
                        )
                    },
                    updateTimeDialogState = { show: Boolean ->
                        viewModel.updateTimeDialogState(
                            show
                        )
                    },
                    updateSwitchState = { state: Boolean -> viewModel.updateSwitchState(state) },
                    onTextChange = { text: String -> viewModel.onTextChange(text) },
                    onPriorityChange = { priority: Priority -> viewModel.onPriorityChange(priority) },
                    onToastMessageStateChange = { text: String? ->
                        viewModel.onToastMessageStateChange(
                            text
                        )
                    },
                    changeDeleteDialogState = { state: Boolean ->
                        viewModel.changeDeleteDialogState(state)
                    },
                    onDateSelected = { date: Long ->
                        viewModel.onDateSelected(date)
                    },
                    onTimeSelected = { hour: Int, minute: Int ->
                        viewModel.onTimeSelected(hour, minute)
                    }
                )
            }
        }

        if (viewModel.showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    viewModel.deleteTodo()
                    onPopBackStack()
                },
                onDismiss = {
                    viewModel.changeDeleteDialogState(false)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    padding: PaddingValues,
    toastMessageState: String?,
    todoUiState: TodoUI,
    dateDialogState: Boolean,
    timeDialogState: Boolean,
    switchState: Boolean,
    updateDateDialogState: (Boolean) -> Unit,
    updateTimeDialogState: (Boolean) -> Unit,
    updateSwitchState: (Boolean) -> Unit,
    onTextChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onToastMessageStateChange: (String?) -> Unit,
    changeDeleteDialogState: (Boolean) -> Unit,
    onDateSelected: (Long) -> Unit,
    onTimeSelected: (Int, Int) -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {

                TextFieldComponent(text = todoUiState.text, onTextChange = onTextChange)

                PriorityComponent(
                    priority = todoUiState.priority,
                    onPriorityChange = onPriorityChange,
                )

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = ToDoListTheme.customColorsPalette.support_separatord,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                DeadlineComponent(
                    deadline = todoUiState.deadLine,
                    switchState = switchState,
                    updateSwitchState = updateSwitchState,
                    updateDateTimeDialogState = updateDateDialogState,
                )
            }

            HorizontalDivider(
                thickness = 0.5.dp,
                color = ToDoListTheme.customColorsPalette.support_separatord,
                modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
            )

            DeleteTodoComponent(
                isButtonEnabled = true,
                onDeleteButtonClick = {
                    changeDeleteDialogState(true)
                }
            )
        }
    }

    if (dateDialogState) {
        DatePickerDialog(
            showDateDialog = dateDialogState,
            updateDateDialogState = updateDateDialogState,
            updateSwitchState = updateSwitchState,
            onDateChange = onDateSelected,
            date = todoUiState.deadLine
        )
    }

    if (timeDialogState) {
        TimePickerDialog(
            onConfirm = { timePickerState ->
                onTimeSelected(timePickerState.hour, timePickerState.minute)
            },
            onCancel = {
                updateTimeDialogState(false)
                updateSwitchState(false)
            },
            time = getCurrentTimeString()
        )
    }

    toastMessageState?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        onToastMessageStateChange(null)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    onPopBackStack: () -> Unit,
    onSaveButtonClick: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ToDoListTheme.customColorsPalette.back_primary
        ),
        title = { Text("") },
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(uikitR.drawable.close),
                contentDescription = "close",
                modifier = Modifier.padding(16.dp).clickable { onPopBackStack() }
            )
        },
        actions = {
            TextButton(
                onClick = {
                    onSaveButtonClick()
                    onPopBackStack()
                },
                modifier = Modifier.padding(top = 17.dp, bottom = 15.dp, end = 16.dp)
            ) {
                Text(
                    text = "СОХРАНИТЬ",
                    color = ToDoListTheme.customColorsPalette.color_blue,
                    style = ToDoListTheme.typography.bodyLarge,
                )
            }
        }
    )
}