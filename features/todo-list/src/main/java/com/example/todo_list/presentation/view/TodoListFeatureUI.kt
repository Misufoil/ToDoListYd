package com.example.todo_list.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_uikit.R
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todo_list.models.TodoUI
import com.example.todo_list.presentation.viewmodel.State
import com.example.todo_list.presentation.viewmodel.TodoListViewModel
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import com.example.app_uikit.R as uikitR


@OptIn(ExperimentalToolbarApi::class)
@Composable
fun TodoListFeatureUI(
    modifier: Modifier = Modifier,
    navigateToAddEdit: (Int) -> Unit,
    padding: PaddingValues,
) {
    val viewModel: TodoListViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
    val currentState = state.value
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val snackBarHostState = remember { SnackbarHostState() }

    CollapsingToolbarScaffold(
        modifier = modifier
            .padding(padding)
            .fillMaxSize(),
        state = collapsingState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            val textSize = (20 + (32 - 20) * collapsingState.toolbarState.progress).sp

            Box(
                modifier = Modifier
                    .background(ToDoListTheme.customColorsPalette.back_primary)
                    .fillMaxWidth()
                    .height(116.dp)
                    .pin()
            )

            Text(
                text = "Мои дела",
                fontSize = textSize,
                color = ToDoListTheme.customColorsPalette.label_primary,
                modifier = Modifier
                    .road(Alignment.TopStart, Alignment.BottomEnd)
                    .padding(start = 16.dp, end = 154.dp, top = 16.dp, bottom = 28.dp)
            )

            if (collapsingState.toolbarState.progress > 0f) {
                Text(
                    text = "Выполнено – ${viewModel.completedTasks.collectAsState().value}", // Example: Replace with dynamic count
                    style = ToDoListTheme.typography.bodyMedium,
                    color = ToDoListTheme.customColorsPalette.label_tertiary,
                    modifier = Modifier
                        .road(Alignment.TopStart, Alignment.BottomStart)
                        .padding(start = 60.dp, top = 92.dp, bottom = 2.dp)
                )
            }

            IconButton(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .road(Alignment.TopEnd, Alignment.BottomEnd)
                    .padding(start = 16.dp, top = 16.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.visibility),
                    tint = ToDoListTheme.customColorsPalette.color_blue,
                    contentDescription = "Visibility"
                )
            }
        }
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
                .background(ToDoListTheme.customColorsPalette.back_primary)
        ) {
            when (currentState) {
                is State.None -> Unit
                is State.Error -> ErrorMessage(state = currentState, modifier = modifier)
                is State.Loading -> ProgressIndicator(currentState, modifier)
                is State.Success -> TodoList(
                    navigateToAddEdit = navigateToAddEdit,
                    todosList = currentState.todos,
                    completeTodo = { todo: TodoUI -> viewModel.completeTodo(todo) },
                    deleteTodo = { todo: TodoUI -> viewModel.deleteTodo(todo) },
                    undoDelete = { viewModel.undoDelete() },
                    snackBarHostState = snackBarHostState
                )
            }
        }

        FloatingActionButton(
            onClick = {
                navigateToAddEdit(-1)
            },
            shape = CircleShape,
            containerColor = ToDoListTheme.customColorsPalette.color_blue,
            contentColor = ToDoListTheme.customColorsPalette.color_white,
            modifier = Modifier.padding(bottom = 12.dp, end = 16.dp).size(56.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(uikitR.drawable.add),
                contentDescription = "Floating action button",
                tint = ToDoListTheme.customColorsPalette.color_white
            )
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = ToDoListTheme.customColorsPalette.back_secondary,
                    contentColor = ToDoListTheme.customColorsPalette.label_secondary, // Изменение цвета фона
                    actionColor = ToDoListTheme.customColorsPalette.color_blue, // Цвет текста кнопки // Настройка цвета background Snackbar
                )
            }
        )
    }
}

@Suppress("NonSkippableComposable")
@Composable
private fun TodoList(
    navigateToAddEdit: (Int) -> Unit,
    todosList: List<TodoUI>,
    completeTodo: (TodoUI) -> Unit,
    deleteTodo: (TodoUI) -> Unit,
    undoDelete: () -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Surface(
        shadowElevation = 3.dp,
        shape = ToDoListTheme.shapes.small,
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp),
        color = ToDoListTheme.customColorsPalette.back_secondary
    ) {
        LazyColumn(modifier = modifier.padding(vertical = 8.dp)) {
            items(items = todosList, key = { item: TodoUI -> item.id!! }) { todoItem ->
                val delete = SwipeAction(
                    onSwipe = {
                        coroutineScope.launch {
                            snackBarHostState.currentSnackbarData?.dismiss()

                            deleteTodo(todoItem)

                            snackBarHostState
                                .showSnackbar(
                                    message = "Заметка удалена",
                                    actionLabel = "Отмена",
                                    duration = SnackbarDuration.Short,
                                ).let { result ->
                                    if (result == SnackbarResult.ActionPerformed) {
                                        undoDelete()
                                    }
                                }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(uikitR.drawable.delete),
                            modifier = Modifier.padding(start = 24.dp),
                            tint = ToDoListTheme.customColorsPalette.color_white,
                            contentDescription = stringResource(id = uikitR.string.delete_todo)
                        )
                    },
                    background = ToDoListTheme.customColorsPalette.color_red
                )

                val complete = SwipeAction(
                    onSwipe = { completeTodo(todoItem) },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(uikitR.drawable.check),
                            modifier = Modifier.padding(end = 24.dp),
                            tint = ToDoListTheme.customColorsPalette.color_white,
                            contentDescription = stringResource(id = uikitR.string.complete_todo)
                        )
                    },
                    background = ToDoListTheme.customColorsPalette.color_green
                )

                SwipeableActionsBox(
                    modifier = modifier.clickable {
                        todoItem.id?.let { navigateToAddEdit(it) }
                    },
                    swipeThreshold = 100.dp,
                    startActions = if (todoItem.isDone) emptyList() else listOf(complete),
                    endActions = listOf(delete)

                ) {
                    TodoItemComponent(
                        todo = todoItem,
                    )
                }
            }

            item {
                DefaultTodoItemComponent()
            }
        }
    }
}


