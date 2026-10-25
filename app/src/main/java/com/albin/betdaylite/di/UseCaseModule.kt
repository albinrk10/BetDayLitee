package com.albin.betdaylite.di

import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.domain.repository.WalletRepository
import com.albin.betdaylite.domain.usecases.DepositFundsUseCase
import com.albin.betdaylite.domain.usecases.GetBetsUseCase
import com.albin.betdaylite.domain.usecases.GetFeaturedMatchUseCase
import com.albin.betdaylite.domain.usecases.GetMatchDetailUseCase
import com.albin.betdaylite.domain.usecases.GetMatchesUseCase
import com.albin.betdaylite.domain.usecases.GetWalletBalanceUseCase
import com.albin.betdaylite.domain.usecases.PlaceBetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMatchesUseCase(
        matchRepository: MatchRepository
    ): GetMatchesUseCase = GetMatchesUseCase(matchRepository)

    @Provides
    @Singleton
    fun provideGetFeaturedMatchUseCase(
        matchRepository: MatchRepository
    ): GetFeaturedMatchUseCase = GetFeaturedMatchUseCase(matchRepository)

    @Provides
    @Singleton
    fun provideGetMatchDetailUseCase(
        matchRepository: MatchRepository
    ): GetMatchDetailUseCase = GetMatchDetailUseCase(matchRepository)

    @Provides
    @Singleton
    fun providePlaceBetUseCase(
        betRepository: BetRepository,
        walletRepository: WalletRepository
    ): PlaceBetUseCase = PlaceBetUseCase(betRepository, walletRepository)

    @Provides
    @Singleton
    fun provideGetBetsUseCase(
        betRepository: BetRepository
    ): GetBetsUseCase = GetBetsUseCase(betRepository)

    @Provides
    @Singleton
    fun provideGetWalletBalanceUseCase(
        walletRepository: WalletRepository
    ): GetWalletBalanceUseCase = GetWalletBalanceUseCase(walletRepository)

    @Provides
    @Singleton
    fun provideDepositFundsUseCase(
        walletRepository: WalletRepository
    ): DepositFundsUseCase = DepositFundsUseCase(walletRepository)
}
