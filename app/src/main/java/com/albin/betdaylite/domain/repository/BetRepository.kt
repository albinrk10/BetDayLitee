package com.albin.betdaylite.domain.repository

import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetStatus
import kotlinx.coroutines.flow.Flow

interface BetRepository {
    fun getAllBets(): Flow<List<Bet>>
    fun getBetsByStatus(status: BetStatus): Flow<List<Bet>>
    suspend fun placeBet(bet: Bet): Result<Unit>
    suspend fun getBetById(betId: String): Bet?
    suspend fun refreshBets(): Result<Unit>
}
