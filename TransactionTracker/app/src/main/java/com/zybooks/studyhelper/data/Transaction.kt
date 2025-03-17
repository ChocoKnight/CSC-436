package com.zybooks.studyhelper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    GROCERIES, TAKEOUT, TRANSPORTATION, UTILITIES, HOUSING, ENTERTAINMENT, PERSONAL_CARE, HEALTHCARE, SAVINGS, PERSONAL, MISC
}

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType = TransactionType.MISC,
    val name: String = "",
    val location: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    @ColumnInfo(name = "created")
    var creationTime: Long = System.currentTimeMillis()
)