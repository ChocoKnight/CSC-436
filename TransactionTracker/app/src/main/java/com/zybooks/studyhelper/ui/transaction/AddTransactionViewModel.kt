package com.zybooks.studyhelper.ui.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.zybooks.studyhelper.StudyHelperApplication
import com.zybooks.studyhelper.TransactionHelperApplication
import com.zybooks.studyhelper.data.Transaction
import com.zybooks.studyhelper.data.TransactionRepository
import com.zybooks.studyhelper.ui.Routes

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

//    private val subjectId: Long = savedStateHandle.toRoute<Routes.AddQuestion>().subjectId

    var transaction by mutableStateOf(Transaction(0))
        private set

    fun changeTransaction(purchase: Transaction) {
        transaction = purchase
    }

    fun addTransaction() {
        transactionRepo.addTransaction(transaction)
    }
}