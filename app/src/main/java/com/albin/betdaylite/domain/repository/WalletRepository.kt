package com.albin.betdaylite.domain.repository

import com.albin.betdaylite.domain.entities.UserWallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getWallet(): Flow<UserWallet>
    suspend fun deductBalance(amount: Double): Result<Unit>
    suspend fun depositFunds(amount: Double): Result<Unit>
}
