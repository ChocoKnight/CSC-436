package com.zybooks.studyhelper.ui.transaction

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTransactionViewModel (
    savedStateHandle: SavedStateHandle,
    private val transactionRepo: TransactionRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TransactionHelperApplication)
                AddTransactionViewModel(this.createSavedStateHandle(), application.transactionRepository)
            }
        }
    }

    var transaction by mutableStateOf(Transaction(0))
        private set

    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepo.getTransaction(transactionId).collect { loadedTransaction ->
                if (loadedTransaction != null) {
                    transaction = loadedTransaction
                }
            }
        }
    }

    fun changeTransaction(newTransaction: Transaction) {
        transaction = newTransaction
    }

    fun addTransaction() {
        transactionRepo.addTransaction(transaction)
    }

    fun updateTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Ensure the update operation is run on the background thread (IO thread)
                transactionRepo.updateTransaction(transaction)
            } catch (e: Exception) {
                // Handle error (optional)
                Log.e("TransactionUpdate", "Failed to update transaction", e)
            }
        }
    }
}