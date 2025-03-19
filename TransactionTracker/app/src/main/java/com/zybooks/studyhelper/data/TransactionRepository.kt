package com.zybooks.studyhelper.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionRepository(context: Context) {
    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            CoroutineScope(Dispatchers.IO).launch {
//                addStarterData()
            }
        }
    }

    private val database: TransactionDatabase = Room.databaseBuilder(
        context,
        TransactionDatabase::class.java,
        "transaction.db"
    )
        .addCallback(databaseCallback)
        .build()

    private val transactionDao = database.transactionDao()

    fun getTransactions() = transactionDao.getTransactions()

    fun getTransaction(transactionId: Long) = transactionDao.getTransaction(transactionId)

    fun getTransactionByWeek(): Flow<List<Transaction>> {
        val currentTime = System.currentTimeMillis()
        val sevenDaysAgo = currentTime - (7 * 24 * 60 * 60 * 1000) // 7 days in milliseconds

        return transactionDao.getTransactionsWithinDateRange(sevenDaysAgo, currentTime)
    }

    fun addTransaction(transaction: Transaction) {
        CoroutineScope(Dispatchers.IO).launch {
            transaction.id = transactionDao.addTransaction(transaction)
        }
    }
}