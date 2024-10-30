package com.example.todo_add_edit.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todo_utils.Priority
import com.example.app_uikit.R as uikitR

@Composable
fun TextFieldComponent(
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = { onTextChange(it) },
        placeholder = { Text(text = stringResource(uikitR.string.text_field_placeholder)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 12.dp)
            .heightIn(min = 104.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = ToDoListTheme.customColorsPalette.back_secondary,
            focusedContainerColor = ToDoListTheme.customColorsPalette.back_secondary,
            focusedPlaceholderColor = ToDoListTheme.customColorsPalette.label_tertiary,
            unfocusedPlaceholderColor = ToDoListTheme.customColorsPalette.label_tertiary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityComponent(
    priority: Priority,
    onPriorityChange: (Priority) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
    ) {
        Column(modifier = Modifier
            .padding(top = 26.dp)
            .clickable { }
            .menuAnchor()
        ) {
            Text(
                text = stringResource(uikitR.string.text_priority),
                style = ToDoListTheme.typography.bodyMedium,
                color = ToDoListTheme.customColorsPalette.label_primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = priority.str,
                style = ToDoListTheme.typography.bodySmall,
                color = when (priority) {
                    Priority.HIGH -> {
                        ToDoListTheme.customColorsPalette.color_red
                    }

                    Priority.NORMAL -> {
                        ToDoListTheme.customColorsPalette.label_tertiary
                    }

                    Priority.LOW -> {
                        ToDoListTheme.customColorsPalette.label_primary
                    }
                }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = ToDoListTheme.customColorsPalette.back_secondary)
        ) {
            Priority.entries.forEach { priorityOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            priorityOption.str,
                            color = if (priorityOption == Priority.HIGH)
                                ToDoListTheme.customColorsPalette.color_red
                            else ToDoListTheme.customColorsPalette.label_primary
                        )
                    },
                    onClick = {
                        onPriorityChange(priorityOption)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun DeadlineComponent(
    deadline: String?,
    switchState: Boolean,
    updateSwitchState: (Boolean) -> Unit,
    updateDateTimeDialogState: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp).clickable {
                updateDateTimeDialogState(true)
            }
        ) {
            Text(
                text = "Сделать до",
                style = ToDoListTheme.typography.bodyMedium,
                color = ToDoListTheme.customColorsPalette.label_primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = deadline ?: "",
                style = ToDoListTheme.typography.bodySmall,
                color = ToDoListTheme.customColorsPalette.color_blue,
                modifier = Modifier.alpha(if (deadline != null) 1f else 0f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = switchState,
            onCheckedChange = {
                updateSwitchState(it)
                updateDateTimeDialogState(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ToDoListTheme.customColorsPalette.color_blue,
                checkedTrackColor = ToDoListTheme.customColorsPalette.color_blue.copy(alpha = 0.4f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.LightGray
            ),
        )
    }
}

@Composable
fun DeleteTodoComponent(
    isButtonEnabled: Boolean,
    onDeleteButtonClick: () -> Unit,
) {
    TextButton(
        onClick = { onDeleteButtonClick() },
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.padding(bottom = 47.dp),
        enabled = isButtonEnabled
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(uikitR.drawable.delete),
            tint = if (isButtonEnabled) ToDoListTheme.customColorsPalette.color_red
            else ToDoListTheme.customColorsPalette.label_tertiary,
            contentDescription = "Delete button",
        )
        Text(
            text = "Удалить",
            color = if (isButtonEnabled) ToDoListTheme.customColorsPalette.color_red
            else ToDoListTheme.customColorsPalette.label_tertiary,
            style = ToDoListTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 17.dp)
        )
    }
}