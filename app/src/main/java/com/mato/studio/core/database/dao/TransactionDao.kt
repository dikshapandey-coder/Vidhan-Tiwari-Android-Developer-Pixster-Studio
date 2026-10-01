package com.mato.studio.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.mato.studio.core.database.BalanceEntity
import com.mato.studio.core.database.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Query("SELECT * FROM balance WHERE id = 1")
    fun observeBalance(): Flow<BalanceEntity?>

    @Query("SELECT * FROM balance WHERE id = 1")
    suspend fun getBalance(): BalanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBalance(balance: BalanceEntity)
}