package com.albin.betdaylite.domain.usecases

import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.repository.MatchRepository
import javax.inject.Inject

class GetMatchDetailUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(matchId: String): Match? {
        return matchRepository.getMatchById(matchId)
    }
}
