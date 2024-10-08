package com.example.todolistyd.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app_uikit.theme.ToDoListTheme
import com.example.todo_list.presentation.view.TodoListFeatureUI
import com.example.todo_list.view.TodoAddEditFeature
import com.example.todo_utils.Routes.ADD_EDIT_SCREEN
import com.example.todo_utils.Routes.LIST_SCREEN

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
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
                    navController.navigate("$ADD_EDIT_SCREEN?todoId=$todoId")
                }
            )
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