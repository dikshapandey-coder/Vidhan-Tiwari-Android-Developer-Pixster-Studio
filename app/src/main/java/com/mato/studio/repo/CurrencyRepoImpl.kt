package com.mato.studio.repo

import com.mato.studio.BuildConfig
import com.mato.studio.core.database.BalanceEntity
import com.mato.studio.core.database.TransactionEntity
import com.mato.studio.core.database.dao.TransactionDao
import com.mato.studio.network.ApiService
import com.mato.studio.network.response.Currency
import com.mato.studio.network.response.TransactionModel
import com.mato.studio.network.response.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

class CurrencyRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: TransactionDao
) : CurrencyRepository {

    override fun observeBalance(): Flow<Double> {
        return dao.observeBalance().map { entity ->
            entity?.balanceInr ?: 0.0
        }
    }

    override fun observeTransactions(): Flow<List<TransactionModel>> {
        return dao.observeTransactions().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun deposit(currency: Currency, amount: Double): Result<Double> {
        return try {
            if (amount <= 0) {
                return Result.failure(IllegalArgumentException("Amount must be greater than 0"))
            }

            val convertedInr: Double = if (currency == Currency.INR) {
                amount
            } else {
                val response = apiService.convertPairAmount(
                    apiKey = BuildConfig.API_KEY,
                    base = currency.code,
                    target = "INR",
                    amount = amount
                )

                if (!response.isSuccessful || response.body() == null) {
                    val errorMsg = response.errorBody()?.string() ?: "Failed to connect to currency conversion service"
                    return Result.failure(Exception("Conversion API error: $errorMsg"))
                }

                val body = response.body()!!
                if (body.result != "success") {
                    val errType = body.errorType ?: "Unknown error"
                    return Result.failure(Exception("Currency API Error: $errType"))
                }

                body.conversionResult ?: (amount * (body.conversionRate ?: 1.0))
            }

            val currentBalance = dao.getBalance()?.balanceInr ?: 0.0
            val newBalance = currentBalance + convertedInr
            dao.saveBalance(BalanceEntity(id = 1, balanceInr = newBalance))

            val transactionCode = generateTransactionCode()
            val entity = TransactionEntity(
                transactionCode = transactionCode,
                type = TransactionType.DEPOSIT.name,
                originalAmount = amount,
                currencyCode = currency.code,
                currencySymbol = currency.symbol,
                amountInr = convertedInr,
                timestamp = System.currentTimeMillis()
            )
            dao.insertTransaction(entity)

            Result.success(convertedInr)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun withdraw(amountInr: Double): Result<Double> {
        return try {
            if (amountInr <= 0) {
                return Result.failure(IllegalArgumentException("Amount must be greater than 0"))
            }

            val currentBalance = dao.getBalance()?.balanceInr ?: 0.0
            if (amountInr > currentBalance) {
                return Result.failure(
                    IllegalArgumentException(
                        "Insufficient funds. Current balance is ₹${"%,.2f".format(currentBalance)}"
                    )
                )
            }

            val newBalance = currentBalance - amountInr
            dao.saveBalance(BalanceEntity(id = 1, balanceInr = newBalance))

            val transactionCode = generateTransactionCode()
            val entity = TransactionEntity(
                transactionCode = transactionCode,
                type = TransactionType.WITHDRAWAL.name,
                originalAmount = amountInr,
                currencyCode = Currency.INR.code,
                currencySymbol = Currency.INR.symbol,
                amountInr = amountInr,
                timestamp = System.currentTimeMillis()
            )
            dao.insertTransaction(entity)

            Result.success(newBalance)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateTransactionCode(): String {
        val letters = ('A'..'Z').toList()
        val randomLetter1 = letters[Random.nextInt(letters.size)]
        val randomLetter2 = letters[Random.nextInt(letters.size)]
        val randomNum = Random.nextInt(1000, 9999)
        val shortId = UUID.randomUUID().toString().take(4).uppercase()
        return "ID$randomNum$randomLetter1$randomLetter2$shortId".take(11)
    }

    private fun TransactionEntity.toModel(): TransactionModel {
        return TransactionModel(
            id = id,
            transactionCode = transactionCode,
            type = if (type == TransactionType.DEPOSIT.name) TransactionType.DEPOSIT else TransactionType.WITHDRAWAL,
            originalAmount = originalAmount,
            currencyCode = currencyCode,
            currencySymbol = currencySymbol,
            amountInr = amountInr,
            timestamp = timestamp
        )
    }
}