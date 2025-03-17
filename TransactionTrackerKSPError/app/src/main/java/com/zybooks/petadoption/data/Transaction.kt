package com.zybooks.petadoption.data

enum class TransactionType {
    GROCERIES, TAKEOUT, TRANSPORTATION, UTILITIES, HOUSING, ENTERTAINMENT, PERSONALCARE, HEALTHCARE, SAVINGS, PERSONAL, MISC
}

data class Transaction(
    val id: Int = 0,
    val type: TransactionType = TransactionType.MISC,
    val name: String = "",
    val location: String = "",
    val amount: Double = 0.0,
    val description: String = "",
)