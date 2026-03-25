package com.albin.betdaylite.domain.repository

import com.albin.betdaylite.domain.model.Bet
import com.albin.betdaylite.domain.model.BetStatus
import com.albin.betdaylite.domain.model.Match
import kotlinx.coroutines.flow.Flow



interface BetRepository {
    
    /**
     * Guarda una nueva apuesta
     * Responsabilidad: Persistencia local + sincronización opcional
     */
    suspend fun saveBet(bet: Bet)
    
    /**
     * Obtiene todas las apuestas del usuario
     * Observable: UI se actualiza automáticamente
     */
    fun getAllBets(): Flow<List<Bet>>
    
    /**
     * Filtra apuestas por estado
     */
    fun getBetsByStatus(status: BetStatus): Flow<List<Bet>>
    
    /**
     * Obtiene una apuesta específica
     */
    suspend fun getBetById(betId: String): Bet?

    /**
     * Carga apuestas iniciales desde fuente remota/local mock cuando la BD está vacía.
     */
    suspend fun ensureBetsBootstrapped()
}

interface MatchRepository {
    
    /**
     * Carga partidos (desde API local/remota)
     */
    suspend fun loadMatches()
    
    /**
     * Obtiene todos los partidos cargados
     */
    fun getMatches(): Flow<List<Match>>
    
    /**
     * Obtiene un partido específico
     */
    suspend fun getMatchById(matchId: String): Match?

    /**
     * Carga partidos iniciales desde fuente remota/local mock cuando la BD está vacía.
     */
    suspend fun ensureMatchesBootstrapped()
}

