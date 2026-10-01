package com.mato.studio.core.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.mato.studio.core.database.dao.AppStateDao
import com.mato.studio.core.database.dao.TransactionDao
import com.mato.studio.core.database.entity.AppState

@Database(entities = [AppState::class, BalanceEntity::class, TransactionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appStateDao(): AppStateDao
    abstract fun transactionDao(): TransactionDao
}