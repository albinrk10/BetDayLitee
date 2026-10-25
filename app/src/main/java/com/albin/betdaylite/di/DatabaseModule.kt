package com.albin.betdaylite.di

import android.content.Context
import com.albin.betdaylite.infrastructure.local.dao.BetDao
import com.albin.betdaylite.infrastructure.local.dao.MatchDao
import com.albin.betdaylite.infrastructure.local.dao.WalletDao
import com.albin.betdaylite.infrastructure.local.db.BetDayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBetDayDatabase(
        @ApplicationContext context: Context
    ): BetDayDatabase = BetDayDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMatchDao(database: BetDayDatabase): MatchDao = database.matchDao()

    @Provides
    @Singleton
    fun provideBetDao(database: BetDayDatabase): BetDao = database.betDao()

    @Provides
    @Singleton
    fun provideWalletDao(database: BetDayDatabase): WalletDao = database.walletDao()
}
