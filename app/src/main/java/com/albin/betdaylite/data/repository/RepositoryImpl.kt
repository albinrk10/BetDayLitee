package com.albin.betdaylite.data.repository

import com.albin.betdaylite.data.local.dao.BetDao
import com.albin.betdaylite.data.local.dao.MatchDao
import com.albin.betdaylite.data.mapper.BetMapper
import com.albin.betdaylite.data.mapper.MatchMapper
import com.albin.betdaylite.data.remote.api.BetApi
import com.albin.betdaylite.domain.model.Bet
import com.albin.betdaylite.domain.model.BetStatus
import com.albin.betdaylite.domain.model.Match
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class BetRepositoryImpl(
    private val betDao: BetDao,
    private val betMapper: BetMapper,
    private val betApi: BetApi
) : BetRepository {
    
    override suspend fun saveBet(bet: Bet) {
        // Mapea Domain → Entity
        val entity = betMapper.toEntity(bet)
        // Guarda en BD local
        betDao.insertBet(entity)
        // En producción aquí iría sincronización remota
    }
    
    override fun getAllBets(): Flow<List<Bet>> =
        betDao.getAllBets()
            .map { entities ->
                entities.map { betMapper.toDomain(it) }
            }
    
    override fun getBetsByStatus(status: BetStatus): Flow<List<Bet>> =
        betDao.getBetsByStatus(status.name)
            .map { entities ->
                entities.map { betMapper.toDomain(it) }
            }
    
    override suspend fun getBetById(betId: String): Bet? =
        betDao.getBetById(betId)?.let { betMapper.toDomain(it) }

    override suspend fun ensureBetsBootstrapped() {
        if (betDao.getCount() > 0) return
        runCatching {
            val response = betApi.getUserBets()
            val entities = response.bets.map { dto ->
                betMapper.toEntity(betMapper.toDomain(dto))
            }
            betDao.insertBets(entities)
        }
    }
}

class MatchRepositoryImpl(
    private val matchDao: MatchDao,
    private val matchMapper: MatchMapper,
    private val betApi: BetApi
) : MatchRepository {
    
    override suspend fun loadMatches() {
        try {
            // Obtiene del API (mock desde assets)
            val response = betApi.getMatches()
            
            // Mapea DTO → Entity
            val entities = response.matches.map { matchMapper.toEntity(matchMapper.toDomain(it)) }
            
            // Guarda en BD local
            matchDao.insertMatches(entities)
        } catch (e: Exception) {
            // Log error, BD contiene datos anteriores
            println("Error loading matches: ${e.message}")
        }
    }

    override suspend fun ensureMatchesBootstrapped() {
        if (matchDao.getCount() > 0) return
        loadMatches()
    }
    
    override fun getMatches(): Flow<List<Match>> =
        matchDao.getAllMatches()
            .map { entities ->
                entities.map { matchMapper.toDomain(it) }
            }
    
    override suspend fun getMatchById(matchId: String): Match? =
        matchDao.getMatchById(matchId)?.let { matchMapper.toDomain(it) }
}

