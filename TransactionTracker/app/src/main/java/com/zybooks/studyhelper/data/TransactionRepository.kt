package com.zybooks.studyhelper.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class TransactionRepository(context: Context) {
    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            CoroutineScope(Dispatchers.IO).launch {
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

    fun getTransactionByDay(): Flow<List<Transaction>> {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        return transactionDao.getTransactionsWithinDateRange(startOfDay, endOfDay)
    }

    fun getTransactionByWeek(): Flow<List<Transaction>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfWeek = calendar.timeInMillis

        return transactionDao.getTransactionsWithinDateRange(startOfWeek, endOfWeek)
    }

    fun getTotalAmountSpent() = transactionDao.getTotalAmountSpentAllTime()

    fun getAmountSpentByDay(): Flow<Double> {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        return transactionDao.getTotalAmountSpentWithinDateRange(startOfDay, endOfDay)
    }

    fun getAmountSpentByWeek(): Flow<Double> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfWeek = calendar.timeInMillis

        return transactionDao.getTotalAmountSpentWithinDateRange(startOfWeek, endOfWeek)
    }

    fun addTransaction(transaction: Transaction) {
        CoroutineScope(Dispatchers.IO).launch {
            transaction.id = transactionDao.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction) // Update the transaction
    }

    suspend fun deleteTransaction(transactionId: Long) {
        transactionDao.deleteTransaction(transactionId)
    }
}