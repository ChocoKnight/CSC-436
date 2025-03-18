package com.zybooks.studyhelper.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zybooks.studyhelper.ui.transaction.TransactionScreen
import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Home

    @Serializable
    data object Camera

    @Serializable
    data object TransactionHistory

    @Serializable
    data object NewTransaction
}

@Composable
fun TransactionTrackerApp() {
    val navController = rememberNavController()

    NavHost (
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen(navController)
        }
        composable<Routes.Camera> {
            CameraScreen(navController)
        }
        composable<Routes.TransactionHistory> {
            HistoryScreen(navController)
        }
        composable<Routes.NewTransaction> {
            TransactionScreen(navController)
        }
    }
}