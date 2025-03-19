package com.zybooks.studyhelper.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zybooks.studyhelper.data.Transaction
import com.zybooks.studyhelper.data.TransactionType
import com.zybooks.studyhelper.ui.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.Factory
    ),
    onEditClick: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBar(
                title = "Transaction History",
                navController = navController
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButtonAddTransaction(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.transactionList.sortedByDescending { it.creationTime }) { transaction ->
                    TransactionItem(transaction = transaction, onEditClick = { transactionId ->
                        navController.navigate(Routes.EditTransaction(transactionId = transactionId))
                    })
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onEditClick: (Long) -> Unit) {
    val formattedDate = formatDate(transaction.creationTime)

    val backgroundColor = when (transaction.type) {
        TransactionType.GROCERIES -> Color(0xFFF18275)
        TransactionType.TAKEOUT -> Color(0xFFF6C26C)
        TransactionType.TRANSPORTATION -> Color(0xFF99C0F5)
        TransactionType.UTILITIES -> Color(0xFF5fd4ce)
        TransactionType.HOUSING -> Color(0xFFe6eb91)
        TransactionType.ENTERTAINMENT -> Color(0xFFB5A0F8)
        TransactionType.PERSONAL_CARE -> Color(0xFFed9ada)
        TransactionType.HEALTHCARE -> Color(0xFF8c888b)
        TransactionType.SAVINGS -> Color(0xFF77EE8C)
        TransactionType.PERSONAL -> Color(0xFFF8AB85)
        TransactionType.MISC -> Color(0xFFdfe5e6)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Location: ${transaction.location}")
            Text(text = "Name: ${transaction.name}")
            Text(text = "Amount: $${transaction.amount}")
            Text(text = "Description: ${transaction.description}")
            Text(text = "Date: $formattedDate")

            // Clickable text for editing
            TextButton(onClick = {
                onEditClick(transaction.id)
            }) {
                Text("Edit")
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
