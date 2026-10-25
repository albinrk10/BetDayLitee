package com.albin.betdaylite.domain.usecases

import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    operator fun invoke(leagueFilter: String = "Todas"): Flow<List<Match>> {
        return matchRepository.getMatches().map { matches ->
            if (leagueFilter == "Todas") {
                matches
            } else {
                matches.filter { it.league.name.equals(leagueFilter, ignoreCase = true) }
            }
        }
    }
}
