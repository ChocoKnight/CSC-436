package com.zybooks.studyhelper

import android.app.Application
import com.zybooks.studyhelper.data.image.ImageRepository
import com.zybooks.studyhelper.data.transaction.TransactionRepository

class TransactionHelperApplication: Application() {
//     Needed to create ViewModels with the ViewModelProvider.Factory
   lateinit var transactionRepository: TransactionRepository
   lateinit var imageRepository: ImageRepository

   // For onCreate() to run, android:name=".TransactionHelperApplication" must
   // be added to <application> in AndroidManifest.xml
   override fun onCreate() {
      super.onCreate()
      transactionRepository = TransactionRepository(this.applicationContext)
      imageRepository = ImageRepository(this.applicationContext)
   }
}