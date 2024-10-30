package com.example.todo_add_edit.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todo_utils.convertStringDateTimeToTime
import com.example.todo_utils.convertStringDateToLong
import java.time.LocalDate
import java.time.ZoneOffset
import com.example.app_uikit.R as uikitR

@ExperimentalMaterial3Api
@Composable
internal fun DatePickerDialog(
    showDateDialog: Boolean,
    updateDateDialogState: (Boolean) -> Unit,
    updateSwitchState: (Boolean) -> Unit,
    onDateChange: (Long) -> Unit,
    deadline: String?
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis =
        if (deadline != null) {
            convertStringDateToLong(deadline)
        } else {
            LocalDate.now()
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli()
        },

        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis()
            }
        }
    )

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = {
                updateDateDialogState(false)
                updateSwitchState(false)
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dateState.selectedDateMillis != null) {
                            onDateChange(dateState.selectedDateMillis!!)
                        }
                        updateDateDialogState(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ToDoListTheme.customColorsPalette.color_blue
                    )
                ) {
                    Text(text = stringResource(id = uikitR.string.ok))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        updateDateDialogState(false)
                        updateSwitchState(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ToDoListTheme.customColorsPalette.color_blue
                    )
                ) {
                    Text(text = stringResource(id = uikitR.string.cancel))
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = ToDoListTheme.customColorsPalette.back_secondary,
                subheadContentColor = ToDoListTheme.customColorsPalette.color_blue,
            )
        ) {
            DatePicker(
                state = dateState,
                showModeToggle = true,
                colors = DatePickerDefaults.colors(
                    containerColor = ToDoListTheme.customColorsPalette.back_secondary,
                    selectedYearContainerColor = ToDoListTheme.customColorsPalette.color_blue,
                    todayDateBorderColor = ToDoListTheme.customColorsPalette.color_blue,
                    selectedDayContainerColor = ToDoListTheme.customColorsPalette.color_blue,
                )
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun TimePickerDialog(
    time: String,
    onCancel: () -> Unit,
    onConfirm: (TimePickerState) -> Unit,
    modifier: Modifier = Modifier
) {
    var mode: DisplayMode by remember { mutableStateOf(DisplayMode.Picker) }

    val (initialHour, initialMinute) = time.split(":").map { it.toInt() }
    val timeState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    PickerDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = { Text(stringResource(id = uikitR.string.select_time)) },
        buttons = {
            DisplayModeToggleButton(
                displayMode = mode,
                onDisplayModeChange = { mode = it },
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onCancel) {
                Text(stringResource(id = uikitR.string.cancel))
            }
            TextButton(onClick = { onConfirm(timeState) }) {
                Text(stringResource(id = uikitR.string.ok))
            }
        },
    ) {
        val contentModifier = Modifier.padding(horizontal = 24.dp)
        when (mode) {
            DisplayMode.Picker -> TimePicker(modifier = contentModifier, state = timeState)
            DisplayMode.Input -> TimeInput(modifier = contentModifier, state = timeState)
        }
    }
}


@Composable
fun PickerDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
            shape = ToDoListTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CompositionLocalProvider(LocalContentColor provides ToDoListTheme.colorScheme.onSurfaceVariant) {
                    ProvideTextStyle(ToDoListTheme.typography.labelLarge) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 24.dp)
                                .padding(top = 16.dp, bottom = 20.dp),
                        ) {
                            title()
                        }
                    }
                }
                CompositionLocalProvider(LocalContentColor provides AlertDialogDefaults.textContentColor) {
                    content()
                }
                CompositionLocalProvider(LocalContentColor provides ToDoListTheme.colorScheme.primary) {
                    ProvideTextStyle(ToDoListTheme.typography.labelLarge) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, end = 6.dp, start = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        ) {
                            buttons()
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun DisplayModeToggleButton(
    displayMode: DisplayMode,
    onDisplayModeChange: (DisplayMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (displayMode) {
        DisplayMode.Picker -> IconButton(
            modifier = modifier,
            onClick = { onDisplayModeChange(DisplayMode.Input) },
        ) {
            Icon(
                painter = painterResource(id = uikitR.drawable.baseline_keyboard_24),
                contentDescription = stringResource(id = uikitR.string.input_mode),
            )
        }

        DisplayMode.Input -> IconButton(
            modifier = modifier,
            onClick = { onDisplayModeChange(DisplayMode.Picker) },
        ) {
            Icon(
                painter = painterResource(id = uikitR.drawable.baseline_schedule_24),
                contentDescription = stringResource(id = uikitR.string.selection_mode),
            )
        }
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = uikitR.string.todo_delete)) },
        confirmButton = {
            ElevatedButton(
                onClick = { onConfirm() },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = ToDoListTheme.customColorsPalette.color_red,
                    contentColor = ToDoListTheme.customColorsPalette.label_primary,
                )
            ) {
                Text(text = stringResource(id = uikitR.string.delete_button))
            }
        },
        dismissButton = {
            ElevatedButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = ToDoListTheme.customColorsPalette.back_secondary,
                    contentColor = ToDoListTheme.customColorsPalette.label_primary,
                )
            ) {
                Text(text = stringResource(id = uikitR.string.cancel))
            }
        },
        containerColor = ToDoListTheme.customColorsPalette.back_primary
    )
}