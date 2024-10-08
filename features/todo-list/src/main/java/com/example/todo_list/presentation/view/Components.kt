package com.example.todo_list.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todo_list.models.TodoUI
import com.example.todo_utils.Priority
import com.example.app_uikit.R as uikitR


@Composable
internal fun TodoItemComponent(
    todo: TodoUI,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = null,
            modifier = modifier.padding(end = 15.dp).size(18.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = ToDoListTheme.customColorsPalette.color_green,
                uncheckedColor =
                if (todo.priority == Priority.HIGH) ToDoListTheme.customColorsPalette.color_red
                else ToDoListTheme.customColorsPalette.support_separatord
            )

        )

        if (todo.priority != Priority.NORMAL) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    when (todo.priority) {
                        Priority.HIGH -> uikitR.drawable.double_carefully
                        else -> uikitR.drawable.arrow_down
                    }
                ),
                tint = when (todo.priority) {
                    Priority.HIGH -> ToDoListTheme.customColorsPalette.color_red
                    else -> ToDoListTheme.customColorsPalette.color_gray
                },
                modifier = modifier.padding(end = 6.dp),
                contentDescription = when (todo.priority) {
                    Priority.HIGH -> "High priority"
                    else -> "Low priority"
                }
            )
        }

        Column(modifier = Modifier.padding(end = 14.dp).weight(1f)) {
            Text(
                text = todo.text,
                style = ToDoListTheme.typography.bodyMedium.copy(
                    textDecoration = if (todo.isDone)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None,
                    color = if (todo.isDone)
                        ToDoListTheme.customColorsPalette.label_tertiary
                    else
                        ToDoListTheme.customColorsPalette.label_primary
                ),
                modifier = modifier,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (todo.deadLine != null) {
                Text(
                    text = todo.deadLine,
                    style = ToDoListTheme.typography.bodySmall,
                    color = ToDoListTheme.customColorsPalette.label_tertiary,
                    modifier = modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(uikitR.drawable.info_outline),
            tint = ToDoListTheme.customColorsPalette.label_tertiary,
            modifier = modifier
                .padding(2.dp)
                .size(20.dp),

            contentDescription = null
        )
    }
}

@Composable
internal fun DefaultTodoItemComponent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 52.dp, end = 16.dp, top = 14.dp, bottom = 14.dp)
    ) {
        Text(
            text = "Новое",
            style = ToDoListTheme.typography.bodyMedium,
            color = ToDoListTheme.customColorsPalette.label_tertiary,
        )
    }
}