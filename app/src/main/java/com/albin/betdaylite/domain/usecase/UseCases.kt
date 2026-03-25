package com.albin.betdaylite.domain.usecase

import com.albin.betdaylite.domain.model.Bet
import com.albin.betdaylite.domain.model.BetPick
import com.albin.betdaylite.domain.model.BetStatus
import com.albin.betdaylite.domain.model.Match
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import java.util.UUID



class PlaceBetUseCase(
    private val betRepository: BetRepository,
    private val matchRepository: MatchRepository
) {
    
    /**
     * Realiza una apuesta
     *
     * Flujo:
     * 1. Valida stake y odd
     * 2. Verifica que el match existe
     * 3. Crea dominio Bet
     * 4. Persiste en BD
     *
     * Result<T>: Manejo explícito de errores (no throws)
     */
    suspend operator fun invoke(
        matchId: String,
        pick: BetPick,
        stake: Double,
        odd: Double
    ): Result<Bet> = runCatching {
        // Validaciones
        require(stake > 0) { "Stake must be positive" }
        require(odd > 0) { "Odd must be positive" }
        
        // Verifica que match existe
        if (matchRepository.getMatchById(matchId) == null) {
            throw IllegalArgumentException("Match not found: $matchId")
        }
        
        // Crea modelo de dominio
        val bet = Bet(
            id = UUID.randomUUID().toString(),
            matchId = matchId,
            pick = pick,
            odd = odd,
            stake = stake,
            status = BetStatus.PENDING,
            placedAt = Instant.now(),
            payout = null
        )
        
        // Persiste
        betRepository.saveBet(bet)
        bet
    }
}

class GetMatchesGroupedByHourUseCase(
    private val matchRepository: MatchRepository
) {
    
    /**
     * Agrupa partidos por hora de inicio
     *
     * Beneficio: Presentación lista para mostrar en UI
     * Clean Code: Conversión de timestamp → hora en un lugar
     */
    operator fun invoke(): Flow<Map<Int, List<Match>>> = matchRepository.getMatches()
        .map { matches ->
            matches.groupBy { match ->
                // Convierte a hora local (Lima)
                match.startTime
                    .atZone(ZoneId.of("America/Lima"))
                    .hour
            }
        }
}

class GetUserBetsUseCase(
    private val betRepository: BetRepository
) {
    
    /**
     * Obtiene todas las apuestas del usuario
     * Observable automático
     */
    operator fun invoke(): Flow<List<Bet>> = betRepository.getAllBets()
}

class GetBetDetailUseCase(
    private val betRepository: BetRepository
) {
    
    /**
     * Obtiene detalle completo de apuesta (bet + match info)
     *
     * Podría devolver un DTO con info de match y bet juntas
     * Por ahora devuelve la bet (match se obtiene por separate)
     */
    suspend fun invoke(betId: String): Result<Bet?> = runCatching {
        betRepository.getBetById(betId)
    }
}

