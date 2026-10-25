package com.albin.betdaylite.di

import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.domain.repository.WalletRepository
import com.albin.betdaylite.infrastructure.repository.BetRepositoryImpl
import com.albin.betdaylite.infrastructure.repository.MatchRepositoryImpl
import com.albin.betdaylite.infrastructure.repository.WalletRepositoryImpl
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
    abstract fun bindMatchRepository(
        impl: MatchRepositoryImpl
    ): MatchRepository

    @Binds
    @Singleton
    abstract fun bindBetRepository(
        impl: BetRepositoryImpl
    ): BetRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        impl: WalletRepositoryImpl
    ): WalletRepository
}
