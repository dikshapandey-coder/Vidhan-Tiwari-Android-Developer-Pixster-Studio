package com.mato.studio.di

import android.app.Application
import androidx.room3.Room
import com.mato.studio.core.database.AppDatabase
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
            "app_database"
        ).build()

    }

    @Provides
    fun provideAppStateDao(database: AppDatabase) = database.appStateDao()
}