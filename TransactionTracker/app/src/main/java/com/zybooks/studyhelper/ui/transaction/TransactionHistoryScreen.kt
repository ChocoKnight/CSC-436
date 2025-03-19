package com.zybooks.studyhelper.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zybooks.studyhelper.data.transaction.Transaction
import com.zybooks.studyhelper.data.transaction.TransactionType
import com.zybooks.studyhelper.ui.*
import com.zybooks.studyhelper.ui.theme.categoryColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.Factory
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val timeFrames = listOf("Daily", "Weekly", "All")
    val categories = listOf("All") + TransactionType.entries

    var selectedTimeFrame by remember { mutableStateOf("Weekly") }
    var selectedCategory by remember { mutableStateOf("All") }

    var expandedTimeFrame by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }

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
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedTimeFrame,
                    onExpandedChange = { expandedTimeFrame = !expandedTimeFrame }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedTimeFrame,
                        onValueChange = {},
                        label = { Text("Select Time Frame") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTimeFrame,
                        onDismissRequest = { expandedTimeFrame = false }
                    ) {
                        timeFrames.forEach { timeFrame ->
                            DropdownMenuItem(
                                text = { Text(timeFrame) },
                                onClick = {
                                    selectedTimeFrame = timeFrame
                                    expandedTimeFrame = false
                                }
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedCategory,
                        onValueChange = {},
                        label = { Text("Select Transaction Type") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.toString()) },
                                onClick = {
                                selectedCategory = category.toString()
                                expandedCategory = false
                            })
                        }
                    }
                }
            }

            val transactionsToShow = when (selectedTimeFrame) {
                "Daily" -> uiState.dailyTransactionList.filter { it.type.name == selectedCategory || selectedCategory == "All" }
                "All" -> uiState.transactionList.filter { it.type.name == selectedCategory || selectedCategory == "All" }
                else -> uiState.weeklyTransactionList.filter { it.type.name == selectedCategory || selectedCategory == "All" }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(transactionsToShow.sortedByDescending { it.creationTime }) { transaction ->
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
