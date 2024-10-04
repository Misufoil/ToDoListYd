package com.example.todo_add_edit.presentation.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.app_uikit.theme.ToDoListTheme
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
    date: String?
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis =
        if (date != null) {
            convertStringDateToLong(date)
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