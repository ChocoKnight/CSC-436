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
import com.zybooks.studyhelper.data.Transaction
import com.zybooks.studyhelper.data.TransactionRepository
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

    val uiState: StateFlow<TransactionListScreenUiState> = transformedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TransactionListScreenUiState(),
        )

    private fun transformedFlow() = combine(
        transactionRepository.getTransactions().filterNotNull(),
        transactionRepository.getTransaction(0)
    ) { transactions, otherData ->
        TransactionListScreenUiState(
            transactionList = transactions,
        )
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transactionId)
        }
    }
}

data class TransactionListScreenUiState(
    val transaction: Transaction = Transaction(),
    val transactionList: List<Transaction> = emptyList()
)