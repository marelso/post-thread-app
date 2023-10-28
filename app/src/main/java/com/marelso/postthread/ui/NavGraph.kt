package com.marelso.postthread.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.marelso.postthread.HomeScreen
import com.marelso.postthread.PostDetailScreen
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

const val KEY_DETAIL_SCREEN = "reference"

sealed class Screen(var route: String) {
    object Home : Screen("home_screen")
    object Detail : Screen("detail_screen/{$KEY_DETAIL_SCREEN}") {
        fun setId(id: Int): String {
            return this.route.replace(
                oldValue = "{$KEY_DETAIL_SCREEN}",
                newValue = id.toString()
            )
        }
    }
}

@Composable
fun setupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navHostController = navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(KEY_DETAIL_SCREEN) {
                type = NavType.IntType
            })
        ) {
            it.arguments?.getInt(KEY_DETAIL_SCREEN)?.let { reference ->
                val viewModel = getViewModel<PostDetailViewModel>(
                    parameters = { parametersOf(90) }
                )

                PostDetailScreen(navHostController = navController, viewModel = viewModel)
            }
        }
    }
}