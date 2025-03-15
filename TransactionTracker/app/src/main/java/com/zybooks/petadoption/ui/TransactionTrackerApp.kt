package com.zybooks.petadoption.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zybooks.petadoption.ui.theme.TransactionTrackerTheme
import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Home

    @Serializable
    data object Camera

    @Serializable
    data object TransactionHistory
}

@Composable
fun TransactionTrackerApp() {
    val navController = rememberNavController()

    NavHost (
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen()
        }
        composable<Routes.Camera> {
            CameraScreen()
        }
        composable<Routes.TransactionHistory> {
            HistoryScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTrackerBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    onUpClick: () -> Unit = { }
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onUpClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            }
        }
    )
}

enum class AppScreen(val route: Any, val title: String, val icon: ImageVector) {
    HOME(Routes.Home, "Home", Icons.Default.Home),
    CAMERA(Routes.Camera, "Camera", Icons.Default.Info),
    TRANSACTION_HISTORY(Routes.TransactionHistory, "Transaction History", Icons.Default.ShoppingCart)
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        AppScreen.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.endsWith(item.route.toString()) == true,
                onClick = {
                    // TODO: Navigate to clicked item's screen
                },
                icon = {
                    Icon(item.icon, contentDescription = item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
//    viewModel: ListViewModel = viewModel()
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TransactionTrackerBar(
                title = "Home"
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
            innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            Text(
                text = "Transactions",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            HorizontalDivider(
                modifier = Modifier.width(300.dp).align(Alignment.CenterHorizontally),
                thickness = 1.dp,
                color = Color.Gray
            )

            Text(
                text = "Budget:  $00.00",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            HorizontalDivider(
                modifier = Modifier.width(300.dp).align(Alignment.CenterHorizontally),
                thickness = 1.dp,
                color = Color.Gray
            )

            Text(
                text = "Please pick up your new family member during business hours.",
                modifier = modifier.padding(6.dp),
            )
        }
    }
}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Camera",
        modifier = modifier.padding(6.dp),
    )
}

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Transactions",
        modifier = modifier.padding(6.dp),
    )
}

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier
) {
    Text(
        text = "History",
        modifier = modifier.padding(6.dp),
    )
}

@Preview
@Composable
fun PreviewHomeScreen() {
    TransactionTrackerTheme() {
        HomeScreen()
    }
}