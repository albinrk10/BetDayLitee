package com.albin.betdaylite.infrastructure.repository

import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.infrastructure.local.dao.MatchDao
import com.albin.betdaylite.infrastructure.remote.api.BetApiService
import com.albin.betdaylite.infrastructure.remote.mapper.MatchMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao,
    private val apiService: BetApiService
) : MatchRepository {

    override fun getMatches(): Flow<List<Match>> {
        return matchDao.getAllMatches()
            .onStart {
                if (matchDao.getCount() == 0) {
                    refreshMatches()
                }
            }
            .map { entities -> entities.map { MatchMapper.toDomain(it) } }
    }

    override suspend fun getMatchById(matchId: String): Match? {
        return matchDao.getMatchById(matchId)?.let { MatchMapper.toDomain(it) }
    }

    override suspend fun refreshMatches(): Result<Unit> {
        return try {
            val response = apiService.getMatches()
            val entities = response.matches.map { MatchMapper.toEntity(it) }
            matchDao.insertMatches(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
