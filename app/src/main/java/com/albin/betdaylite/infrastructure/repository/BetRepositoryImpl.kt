package com.albin.betdaylite.infrastructure.repository

import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetStatus
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.infrastructure.local.dao.BetDao
import com.albin.betdaylite.infrastructure.remote.api.BetApiService
import com.albin.betdaylite.infrastructure.remote.mapper.BetMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BetRepositoryImpl @Inject constructor(
    private val betDao: BetDao,
    private val apiService: BetApiService
) : BetRepository {

    override fun getAllBets(): Flow<List<Bet>> {
        return betDao.getAllBets()
            .onStart {
                if (betDao.getCount() == 0) {
                    refreshBets()
                }
            }
            .map { entities -> entities.map { BetMapper.toDomain(it) } }
    }

    override fun getBetsByStatus(status: BetStatus): Flow<List<Bet>> {
        return betDao.getBetsByStatus(status.name)
            .map { entities -> entities.map { BetMapper.toDomain(it) } }
    }

    override suspend fun placeBet(bet: Bet): Result<Unit> {
        return try {
            val entity = BetMapper.toEntity(bet)
            betDao.insertBet(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBetById(betId: String): Bet? {
        return betDao.getBetById(betId)?.let { BetMapper.toDomain(it) }
    }

    override suspend fun refreshBets(): Result<Unit> {
        return try {
            val response = apiService.getBets()
            val entities = response.bets.map { BetMapper.toEntity(it) }
            betDao.insertBets(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
