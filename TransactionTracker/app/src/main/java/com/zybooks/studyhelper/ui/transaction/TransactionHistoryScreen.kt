package com.zybooks.studyhelper.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zybooks.studyhelper.data.Transaction
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
    onUpClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
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
                items(uiState.transactionList) { transaction ->
                    TransactionItem(transaction = transaction, onEditClick = onEditClick)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onEditClick: (Long) -> Unit) {
    val formattedDate = formatDate(transaction.creationTime)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Location: ${transaction.location}")
            Text(text = "Name: ${transaction.name}")
            Text(text = "Amount: ${transaction.amount}")
            Text(text = "Date: $formattedDate")

            // Clickable text for editing
            TextButton(onClick = { onEditClick(transaction.id) }) {
                Text("Edit")
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
