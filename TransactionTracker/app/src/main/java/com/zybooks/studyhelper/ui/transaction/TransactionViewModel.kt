package com.zybooks.studyhelper.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.zybooks.studyhelper.TransactionHelperApplication
import com.zybooks.studyhelper.data.transaction.Transaction
import com.zybooks.studyhelper.data.transaction.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TransactionHelperApplication)
                TransactionViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    transactionRepository = application.transactionRepository
                )
            }
        }
    }

    val uiState: StateFlow<TransactionsScreenUiState> = transformedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TransactionsScreenUiState(),
        )

    private fun transformedFlow() = combine(
        transformedTransactionsFlow(),
        transformedSpendingFlow()
    ) { transactionUiState, spendingUiState ->
        TransactionsScreenUiState(
            transactionList = transactionUiState.transactionList,
            dailyTransactionList = transactionUiState.dailyTransactionList,
            weeklyTransactionList = transactionUiState.weeklyTransactionList,
            totalSpent = spendingUiState.totalSpent,
            totalSpentByDay = spendingUiState.totalSpentByDay,
            totalSpentByWeek = spendingUiState.totalSpentByWeek
        )
    }

    private fun transformedTransactionsFlow() = combine(
        transactionRepository.getTransactions().filterNotNull(),
        transactionRepository.getTransactionByDay().filterNotNull(),
        transactionRepository.getTransactionByWeek().filterNotNull(),
    ) { transactions, dailyTransactions, weeklyTransactions ->
        TransactionsScreenUiState(
            transactionList = transactions,
            dailyTransactionList = dailyTransactions,
            weeklyTransactionList = weeklyTransactions,
        )
    }

    private fun transformedSpendingFlow() = combine(
        transactionRepository.getTotalAmountSpent().filterNotNull(),
        transactionRepository.getAmountSpentByDay().filterNotNull(),
        transactionRepository.getAmountSpentByWeek().filterNotNull()
    ) { totalSpending, dailySpending, weeklySpending ->
        TransactionsScreenUiState(
            totalSpent = totalSpending,
            totalSpentByDay = dailySpending,
            totalSpentByWeek = weeklySpending
        )
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transactionId)
        }
    }
}

data class TransactionsScreenUiState(
    val transactionList: List<Transaction> = emptyList(),
    val dailyTransactionList: List<Transaction> = emptyList(),
    val weeklyTransactionList: List<Transaction> = emptyList(),
    val totalSpent: Double = 0.0,
    val totalSpentByDay: Double = 0.0,
    val totalSpentByWeek: Double = 0.0
)
