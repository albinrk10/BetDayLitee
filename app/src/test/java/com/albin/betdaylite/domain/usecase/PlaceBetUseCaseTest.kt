package com.albin.betdaylite.domain.usecase

import com.albin.betdaylite.domain.model.BetPick
import com.albin.betdaylite.domain.model.Match
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.Instant

/**
 * Test para PlaceBetUseCase
 *
 * Razón: Unit tests para lógica de negocio
 * SOLID - SRP: Test solo testa use case, no UI ni BD
 * Clean Code: AAA pattern (Arrange, Act, Assert)
 */

class PlaceBetUseCaseTest {
    
    @Mock
    private lateinit var mockBetRepository: BetRepository
    
    @Mock
    private lateinit var mockMatchRepository: MatchRepository
    
    private lateinit var useCase: PlaceBetUseCase
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = PlaceBetUseCase(mockBetRepository, mockMatchRepository)
    }
    
    /**
     * Test: Apuesta válida se guarda correctamente
     *
     * Arrange: Prepara datos de prueba
     * Act: Ejecuta la función
     * Assert: Verifica resultados
     */
    @Test
    fun `placeBet with valid data should save bet to repository`() = runTest {
        // Arrange
        val matchId = "match_001"
        val pick = BetPick.HOME
        val stake = 50.0
        val odd = 2.5
        
        // Mock match repository
        val mockMatch = createMockMatch(matchId)
        whenever(mockMatchRepository.getMatchById(matchId)).thenReturn(mockMatch)
        whenever(mockBetRepository.saveBet(any())).thenReturn(Unit)
        
        // Act
        val result = useCase(matchId, pick, stake, odd)
        
        // Assert
        assertTrue("Bet should be placed successfully", result.isSuccess)
        assertNotNull("Result should contain a Bet", result.getOrNull())
    }
    
    /**
     * Test: Apuesta con stake negativo falla
     */
    @Test
    fun `placeBet with negative stake should fail`() = runTest {
        // Arrange
        val matchId = "match_001"
        val pick = BetPick.HOME
        val invalidStake = -10.0
        val odd = 2.5
        
        // Act
        val result = useCase(matchId, pick, invalidStake, odd)
        
        // Assert
        assertTrue("Bet with negative stake should fail", result.isFailure)
    }
    
    /**
     * Test: Apuesta con stake cero falla
     */
    @Test
    fun `placeBet with zero stake should fail`() = runTest {
        // Arrange
        val matchId = "match_001"
        val pick = BetPick.HOME
        val invalidStake = 0.0
        val odd = 2.5
        
        // Act
        val result = useCase(matchId, pick, invalidStake, odd)
        
        // Assert
        assertTrue("Bet with zero stake should fail", result.isFailure)
    }
    
    private fun createMockMatch(id: String): Match {
        return Match(
            id = id,
            startTime = Instant.parse("2026-02-12T10:00:00Z"),
            league = com.albin.betdaylite.domain.model.League(
                id = "league_001",
                name = "Premier League",
                country = "England"
            ),
            homeTeam = com.albin.betdaylite.domain.model.Team(
                id = "team_001",
                name = "Team A",
                shortName = "TA"
            ),
            awayTeam = com.albin.betdaylite.domain.model.Team(
                id = "team_002",
                name = "Team B",
                shortName = "TB"
            ),
            market = com.albin.betdaylite.domain.model.Market(
                type = "1X2",
                odds = com.albin.betdaylite.domain.model.Odds(
                    home = 2.5,
                    draw = 3.0,
                    away = 2.8
                )
            )
        )
    }
}

