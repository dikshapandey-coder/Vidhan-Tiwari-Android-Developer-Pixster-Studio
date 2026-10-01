package com.mato.studio.core.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "balance")
data class BalanceEntity(
    @PrimaryKey
    val id: Int = 1,
    val balanceInr: Double = 0.0
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transactionCode: String,
    val type: String,
    val originalAmount: Double,
    val currencyCode: String,
    val currencySymbol: String,
    val amountInr: Double,
    val timestamp: Long = System.currentTimeMillis()
)