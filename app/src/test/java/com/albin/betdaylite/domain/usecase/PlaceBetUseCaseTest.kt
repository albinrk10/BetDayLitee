package com.albin.betdaylite.domain.usecase

import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.League
import com.albin.betdaylite.domain.entities.Market
import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.entities.Odds
import com.albin.betdaylite.domain.entities.Team
import com.albin.betdaylite.domain.entities.UserWallet
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.WalletRepository
import com.albin.betdaylite.domain.usecases.PlaceBetUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.Instant

class PlaceBetUseCaseTest {

    @Mock
    private lateinit var mockBetRepository: BetRepository

    @Mock
    private lateinit var mockWalletRepository: WalletRepository

    private lateinit var useCase: PlaceBetUseCase

    private val testMatch = Match(
        id = "match_100",
        startTime = Instant.now(),
        league = League("pl", "Premier League", "England"),
        homeTeam = Team("tot", "Tottenham", "TOT"),
        awayTeam = Team("che", "Chelsea", "CHE"),
        market = Market("1X2", Odds(2.5, 3.2, 2.8))
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = PlaceBetUseCase(mockBetRepository, mockWalletRepository)
    }

    @Test
    fun placeBet_withSufficientBalance_returnsSuccess() = runTest {
        whenever(mockWalletRepository.getWallet()).thenReturn(
            flowOf(UserWallet(balance = 1000.0, totalWagered = 0.0, totalWon = 0.0, activeBetsCount = 0))
        )
        whenever(mockWalletRepository.deductBalance(any())).thenReturn(Result.success(Unit))
        whenever(mockBetRepository.placeBet(any())).thenReturn(Result.success(Unit))

        val result = useCase(
            match = testMatch,
            pick = BetPick.HOME,
            odd = 2.5,
            stake = 50.0
        )

        assertTrue(result.isSuccess)
        val bet = result.getOrNull()
        assertEquals("match_100", bet?.matchId)
        assertEquals(BetPick.HOME, bet?.pick)
        assertEquals(50.0, bet?.stake)
    }

    @Test
    fun placeBet_withInsufficientBalance_returnsFailure() = runTest {
        whenever(mockWalletRepository.getWallet()).thenReturn(
            flowOf(UserWallet(balance = 20.0, totalWagered = 0.0, totalWon = 0.0, activeBetsCount = 0))
        )

        val result = useCase(
            match = testMatch,
            pick = BetPick.HOME,
            odd = 2.5,
            stake = 50.0
        )

        assertTrue(result.isFailure)
        assertEquals("Saldo insuficiente en tu billetera", result.exceptionOrNull()?.message)
    }

    @Test
    fun placeBet_withZeroStake_returnsFailure() = runTest {
        val result = useCase(
            match = testMatch,
            pick = BetPick.HOME,
            odd = 2.5,
            stake = 0.0
        )

        assertTrue(result.isFailure)
    }
}
