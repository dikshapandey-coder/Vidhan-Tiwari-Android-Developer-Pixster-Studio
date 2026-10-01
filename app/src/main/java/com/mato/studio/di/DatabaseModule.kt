package com.mato.studio.di

import android.app.Application
import androidx.room3.Room
import com.mato.studio.core.database.AppDatabase
import com.mato.studio.core.database.dao.AppStateDao
import com.mato.studio.core.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studio_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAppStateDao(database: AppDatabase): AppStateDao = database.appStateDao()

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao = database.transactionDao()
}