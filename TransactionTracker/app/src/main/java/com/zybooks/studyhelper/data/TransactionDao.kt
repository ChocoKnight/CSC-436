package com.zybooks.studyhelper.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("Select * From `Transaction`")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("Select * From `Transaction` Where id = :id")
    fun getTransaction(id: Long): Flow<Transaction?>

    @Query("SELECT * FROM `Transaction` WHERE created BETWEEN :startTimestamp AND :endTimestamp")
    fun getTransactionsWithinDateRange(startTimestamp: Long, endTimestamp: Long): Flow<List<Transaction>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM `Transaction`")
    fun getTotalAmountSpentAllTime(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM `Transaction` WHERE created BETWEEN :startTimestamp AND :endTimestamp")
    fun getTotalAmountSpentWithinDateRange(startTimestamp: Long, endTimestamp: Long): Flow<Double>

    @Insert
    fun addTransaction(transaction: Transaction): Long

    @Update
    fun updateTransaction(transaction: Transaction)

    @Query("DELETE FROM `Transaction` WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: Long)
}