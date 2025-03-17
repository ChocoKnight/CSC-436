package com.zybooks.studyhelper.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("Select * From `Transaction`")
    abstract fun getTransactions(): Flow<List<Transaction>>

    @Query("Select * From `Transaction` Where id = :id")
    abstract fun getTransaction(id: Long): Flow<Transaction?>

    @Query("SELECT * FROM `Transaction` WHERE created BETWEEN :startTimestamp AND :endTimestamp")
    abstract fun getTransactionsWithinDateRange(startTimestamp: Long, endTimestamp: Long): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transaction: Transaction): Long

    @Update
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)
}