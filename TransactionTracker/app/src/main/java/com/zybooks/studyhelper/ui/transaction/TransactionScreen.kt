package com.zybooks.studyhelper.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zybooks.studyhelper.data.transaction.Transaction
import com.zybooks.studyhelper.data.transaction.TransactionType
import com.zybooks.studyhelper.ui.BottomNavigationBar
import com.zybooks.studyhelper.ui.TopBar

@Composable
fun TransactionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AddTransactionViewModel = viewModel(
        factory = AddTransactionViewModel.Factory
    ),
    onSaveClick: () -> Unit = {},
    transactionId: Long? = null
) {
    if (transactionId != null) {
        viewModel.loadTransaction(transactionId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Transaction",
                canNavigateBack = true,
                navController = navController
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (transactionId == null) {
                        viewModel.addTransaction()
                    } else {
                        viewModel.updateTransaction()
                    }
                    onSaveClick()
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = if (transactionId == null) "Add Transaction" else "Update Transaction"
                )
            }
        }
    ) {
        innerPadding ->
        TransactionEntry(
            transaction = viewModel.transaction,
            onTransactionChange = { viewModel.changeTransaction(it) },
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun TransactionEntry(
    transaction: Transaction,
    onTransactionChange: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        val labelWidth = 125.dp

        TransactionField(label = "Location", labelWidth, transaction.location) {
            onTransactionChange(transaction.copy(location = it))
        }

        TransactionField(label = "Name", labelWidth, transaction.name) {
            onTransactionChange(transaction.copy(name = it))
        }

        TransactionField(label = "Description", labelWidth, transaction.description) {
            onTransactionChange(transaction.copy(description = it))
        }

        TransactionField(label = "Amount", labelWidth, transaction.amount.toString()) { newValue ->
            val parsedValue = newValue.toDoubleOrNull() ?: 0.0
            onTransactionChange(transaction.copy(amount = parsedValue))
        }

        TransactionTypeDropdown(transaction.type) { selectedType ->
            onTransactionChange(transaction.copy(type = selectedType))
        }
    }
}

@Composable
fun TransactionField(
    label: String,
    labelWidth: Dp,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            modifier = Modifier
                .width(labelWidth)
                .padding(8.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            maxLines = Int.MAX_VALUE,
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .border(1.dp, Color.Black)
                .background(Color.White)
                .heightIn(min = 56.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeDropdown(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedType.name,
                label = { Text("Select Transaction Type") },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                TransactionType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            onTypeSelected(type)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}