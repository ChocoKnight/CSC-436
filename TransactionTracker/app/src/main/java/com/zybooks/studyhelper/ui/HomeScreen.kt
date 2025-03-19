package com.zybooks.studyhelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zybooks.studyhelper.data.transaction.Transaction
import com.zybooks.studyhelper.data.transaction.TransactionType
import com.zybooks.studyhelper.ui.theme.categoryColors
import com.zybooks.studyhelper.ui.transaction.TransactionViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.Factory
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedOption by remember { mutableStateOf("Weekly") }

    Scaffold(
        topBar = {
            TopBar(
                title = "Home",
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
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { selectedOption = "Daily" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == "Daily") Color.Blue else Color.Gray
                    )
                ) {
                    Text("Daily")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { selectedOption = "Weekly" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == "Weekly") Color.Blue else Color.Gray
                    )
                ) {
                    Text("Weekly")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { selectedOption = "All" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == "All") Color.Blue else Color.Gray
                    )
                ) {
                    Text("All")
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray
            )

            Text(
                text = "Amount Spent",
                modifier = Modifier.padding(8.dp)
            )

            val amountToShow = when (selectedOption) {
                "Daily" -> uiState.totalSpentByDay
                "Weekly" -> uiState.totalSpentByWeek
                "All" -> uiState.totalSpent
                else -> 0.0
            }

            Text(
                text = "$$amountToShow",
                modifier = Modifier.padding(8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray
            )

            if (selectedOption == "All") {
                Text(
                    text = "All Transactions",
                    modifier = Modifier.padding(6.dp),
                )
            } else {
                Text(
                    text = "Recent Transactions ($selectedOption)",
                    modifier = Modifier.padding(6.dp),
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val transactionsToShow = when (selectedOption) {
                    "Daily" -> uiState.dailyTransactionList
                    "Weekly" -> uiState.weeklyTransactionList
                    "All" -> uiState.transactionList
                    else -> emptyList()
                }

                items(transactionsToShow.sortedByDescending { it.creationTime }) { transaction ->
                    MiniTransactionItem(
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
fun MiniTransactionItem(transaction: Transaction, onEditClick: (Long) -> Unit, onDeleteClick: (Long) -> Unit) {
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(text = "Location: ${transaction.location}")
                Text(text = "Name: ${transaction.name}")
                Text(text = "Amount: $${transaction.amount}")
            }

            Column(
                modifier = Modifier
            ) {
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