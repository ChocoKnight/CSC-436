package com.zybooks.studyhelper.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zybooks.studyhelper.ui.camera.CameraScreen
import com.zybooks.studyhelper.ui.transaction.TransactionScreen
import com.zybooks.studyhelper.ui.transaction.HistoryScreen
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

    @Serializable
    data class EditTransaction(
        val transactionId: Long
    )
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
        composable<Routes.EditTransaction> { backStackEntry ->
            val routeArgs = backStackEntry.toRoute<Routes.EditTransaction>()

            TransactionScreen(
                navController = navController,
                transactionId = routeArgs.transactionId, // Pass the transactionId for editing
                onSaveClick = {
                    // Save the edited transaction and pop the back stack
                    navController.popBackStack()
                    navController.navigate(Routes.TransactionHistory)
                }
            )
        }
    }
}