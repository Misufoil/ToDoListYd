package com.example.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todo_add_edit.presentation.view.TodoAddEditFeature
import com.example.todo_list.presentation.view.TodoListFeatureUI
import com.example.todo_utils.Routes.ADD_EDIT_SCREEN
import com.example.todo_utils.Routes.LIST_SCREEN

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startParams: String,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    // Используем флаг для предотвращения повторной навигации
    val hasNavigatedToAddEdit = remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = LIST_SCREEN,
        enterTransition = {
            // you can change whatever you want transition
            EnterTransition.None
        },
        exitTransition = {
            // you can change whatever you want transition
            ExitTransition.None
        }
    ) {
        composable(
            route = LIST_SCREEN,
        ) {
            TodoListFeatureUI(
                modifier = Modifier.background(ToDoListTheme.customColorsPalette.back_primary),
                padding = padding,
                navigateToAddEdit = { todoId ->
                    navController.navigate("$ADD_EDIT_SCREEN?todoId=$todoId") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            if (!hasNavigatedToAddEdit.value && startParams.isNotEmpty()) {
                LaunchedEffect(startParams) {
                    navController.navigate("$ADD_EDIT_SCREEN?todoId=$startParams") {
                        launchSingleTop = true
                    }
                    // Устанавливаем флаг, чтобы предотвратить повторную навигацию
                    hasNavigatedToAddEdit.value = true
                }
            }
        }

        composable(
            route = "$ADD_EDIT_SCREEN?todoId={todoId}",
            arguments = listOf(
                navArgument(name = "todoId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            TodoAddEditFeature(
                onPopBackStack = {
                    navController.popBackStack(LIST_SCREEN, false)
                },
                padding = padding,
            )
        }
    }
}