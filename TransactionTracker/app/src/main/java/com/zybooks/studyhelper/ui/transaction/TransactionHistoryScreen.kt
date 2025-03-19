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
import com.zybooks.studyhelper.ui.theme.categoryColors
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
                    TransactionItem(
                        transaction = transaction,
                        onEditClick = { transactionId ->
                            navController.navigate(Routes.EditTransaction(transactionId = transactionId))
                        },
                        onDeleteClick = { transactionId ->
                            viewModel.deleteTransaction(transactionId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onEditClick: (Long) -> Unit, onDeleteClick: (Long) -> Unit) {
    val formattedDate = formatDate(transaction.creationTime)

    val backgroundColor = when (transaction.type) {
        TransactionType.GROCERIES -> categoryColors[0]
        TransactionType.TAKEOUT -> categoryColors[1]
        TransactionType.TRANSPORTATION -> categoryColors[2]
        TransactionType.UTILITIES -> categoryColors[3]
        TransactionType.HOUSING -> categoryColors[4]
        TransactionType.ENTERTAINMENT -> categoryColors[5]
        TransactionType.PERSONAL_CARE -> categoryColors[6]
        TransactionType.HEALTHCARE -> categoryColors[7]
        TransactionType.SAVINGS -> categoryColors[8]
        TransactionType.PERSONAL -> categoryColors[9]
        TransactionType.MISC -> categoryColors[10]
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick(transaction.id)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        )
        {
            Text(text = "Location: ${transaction.location}")
            Text(text = "Name: ${transaction.name}")
            Text(text = "Type: ${transaction.type}")
            Text(text = "Description: ${transaction.description}")
            Text(text = "Amount: $${transaction.amount}")
            Text(text = "Date: $formattedDate")

            Row {
                TextButton(onClick = {
                    onEditClick(transaction.id)
                }) {
                    Text("Edit")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
