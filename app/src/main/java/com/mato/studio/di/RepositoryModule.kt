package com.mato.studio.di

import com.mato.studio.repo.CurrencyRepoImpl
import com.mato.studio.repo.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(
        currencyRepoImpl: CurrencyRepoImpl
    ): CurrencyRepository
}
