package com.albin.betdaylite.domain.repository

import com.albin.betdaylite.domain.entities.Match
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    fun getMatches(): Flow<List<Match>>
    suspend fun getMatchById(matchId: String): Match?
    suspend fun refreshMatches(): Result<Unit>
}
