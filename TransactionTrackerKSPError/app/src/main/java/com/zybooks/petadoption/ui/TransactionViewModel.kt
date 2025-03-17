package com.zybooks.petadoption.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zybooks.petadoption.data.PetDataSource
import com.zybooks.petadoption.data.Transaction

class TransactionViewModel : ViewModel() {
//   val petList = PetDataSource().loadPets()
   var selectedTransaction by mutableStateOf(Transaction())
}

