package com.albin.betdaylite.di

import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.domain.usecase.GetBetDetailUseCase
import com.albin.betdaylite.domain.usecase.GetMatchesGroupedByHourUseCase
import com.albin.betdaylite.domain.usecase.GetUserBetsUseCase
import com.albin.betdaylite.domain.usecase.PlaceBetUseCase
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
    fun providePlaceBetUseCase(
        betRepository: BetRepository,
        matchRepository: MatchRepository
    ): PlaceBetUseCase = PlaceBetUseCase(betRepository, matchRepository)
    
    @Provides
    @Singleton
    fun provideGetMatchesGroupedByHourUseCase(
        matchRepository: MatchRepository
    ): GetMatchesGroupedByHourUseCase = GetMatchesGroupedByHourUseCase(matchRepository)
    
    @Provides
    @Singleton
    fun provideGetUserBetsUseCase(
        betRepository: BetRepository
    ): GetUserBetsUseCase = GetUserBetsUseCase(betRepository)
    
    @Provides
    @Singleton
    fun provideGetBetDetailUseCase(
        betRepository: BetRepository
    ): GetBetDetailUseCase = GetBetDetailUseCase(betRepository)
}

