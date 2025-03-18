package com.zybooks.studyhelper

import android.app.Application
import com.zybooks.studyhelper.data.TransactionRepository

class TransactionHelperApplication: Application() {
//     Needed to create ViewModels with the ViewModelProvider.Factory
   lateinit var transactionRepository: TransactionRepository

   // For onCreate() to run, android:name=".TransactionHelperApplication" must
   // be added to <application> in AndroidManifest.xml
   override fun onCreate() {
      super.onCreate()
      transactionRepository = TransactionRepository(this.applicationContext)
   }
}