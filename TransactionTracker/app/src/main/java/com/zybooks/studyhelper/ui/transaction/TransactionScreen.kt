package com.zybooks.studyhelper.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zybooks.studyhelper.data.Transaction
import com.zybooks.studyhelper.ui.BottomNavigationBar
import com.zybooks.studyhelper.ui.Routes
import com.zybooks.studyhelper.ui.TopBar
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.studyhelper.ui.theme.TransactionTrackerTheme

@Composable
fun TransactionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AddTransactionViewModel = viewModel(
        factory = AddTransactionViewModel.Factory
    ),
    onUpClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
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
                    viewModel.addTransaction()
                    onSaveClick()
                    navController.navigate(Routes.NewTransaction) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Add Transaction"
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
//    Row (
//        modifier = Modifier.fillMaxWidth().padding(8.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column (
//            modifier = modifier.fillMaxWidth()
//        ){
//            Text(
//                text = "Location",
//                fontSize = 20.sp,
//                modifier = Modifier.padding(8.dp),
//            )
//
//            Text(
//                text = "Name",
//                fontSize = 20.sp,
//                modifier = Modifier.padding(8.dp),
//            )
//
//            Text(
//                text = "Description",
//                fontSize = 20.sp,
//                modifier = Modifier.padding(8.dp),
//            )
//
//            Text(
//                text = "Amount",
//                fontSize = 20.sp,
//                modifier = Modifier.padding(8.dp),
//            )
//        }
//
//        Column (
//            modifier = modifier.fillMaxWidth()
//        ){
//            TextField(
//                value = transaction.location,
//                onValueChange = { onTransactionChange(transaction.copy(location = it)) },
//                singleLine = false,
//                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp)
//                    .border(1.dp, Color.Black)
//                    .background(Color.White)
//            )
//
//            TextField(
//                value = transaction.name,
//                onValueChange = { onTransactionChange(transaction.copy(name = it)) },
//                singleLine = false,
//                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp)
//                    .border(1.dp, Color.Black)
//                    .background(Color.White)
//            )
//
//            TextField(
//                value = transaction.description,
//                onValueChange = { onTransactionChange(transaction.copy(description = it)) },
//                singleLine = false,
//                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp)
//                    .border(1.dp, Color.Black)
//                    .background(Color.White)
//            )
//
//            TextField(
//                value = transaction.amount.toString() ?: "0",
//                onValueChange = { newValue ->
//                    // Allow empty input for user deletions
//                    val parsedValue = newValue.toDoubleOrNull() ?: 0.0
//                    onTransactionChange(transaction.copy(amount = parsedValue))
//                },
//                singleLine = true,
//                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp)
//                    .border(1.dp, Color.Black)
//                    .background(Color.White)
//            )
//        }
//    }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Location",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
            )
            TextField(
                value = transaction.location,
                onValueChange = { onTransactionChange(transaction.copy(location = it)) },
                singleLine = false,
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.Black)
                    .background(Color.White)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Location",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
            )
            TextField(
                value = transaction.name,
                onValueChange = { onTransactionChange(transaction.copy(name = it)) },
                singleLine = false,
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.Black)
                    .background(Color.White)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Description",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
            )
            TextField(
                value = transaction.description,
                onValueChange = { onTransactionChange(transaction.copy(description = it)) },
                singleLine = false,
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.Black)
                    .background(Color.White)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Amount",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
            )
            TextField(
                value = transaction.amount.toString() ?: "0",
                onValueChange = { newValue ->
                    // Allow empty input for user deletions
                    val parsedValue = newValue.toDoubleOrNull() ?: 0.0
                    onTransactionChange(transaction.copy(amount = parsedValue))
                },
                singleLine = true,
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.Black)
                    .background(Color.White)
            )
        }
    }
}