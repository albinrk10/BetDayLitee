package com.albin.betdaylite.domain.usecases

import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFeaturedMatchUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    operator fun invoke(): Flow<Match?> {
        return matchRepository.getMatches().map { matches ->
            // Selecciona un partido estelar (por ejemplo Tottenham o el primero de la lista)
            matches.firstOrNull { it.homeTeam.shortName == "TOT" || it.homeTeam.name.contains("Tottenham", ignoreCase = true) }
                ?: matches.firstOrNull()
        }
    }
}
