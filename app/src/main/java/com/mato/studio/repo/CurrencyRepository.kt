package com.mato.studio.repo

import com.mato.studio.network.response.Currency
import com.mato.studio.network.response.TransactionModel
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun observeBalance(): Flow<Double>
    fun observeTransactions(): Flow<List<TransactionModel>>
    suspend fun deposit(currency: Currency, amount: Double): Result<Double>
    suspend fun withdraw(amountInr: Double): Result<Double>
}