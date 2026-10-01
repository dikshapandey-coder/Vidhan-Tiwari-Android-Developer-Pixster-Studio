package com.mato.studio.core.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.mato.studio.core.database.dao.AppStateDao
import com.mato.studio.core.database.entity.AppState

@Database(entities = [AppState::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appStateDao(): AppStateDao
}