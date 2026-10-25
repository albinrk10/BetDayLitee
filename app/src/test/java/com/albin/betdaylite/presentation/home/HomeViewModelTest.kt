package com.albin.betdaylite.presentation.home

import com.albin.betdaylite.domain.entities.League
import com.albin.betdaylite.domain.entities.Market
import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.entities.Odds
import com.albin.betdaylite.domain.entities.Team
import com.albin.betdaylite.domain.entities.UserWallet
import com.albin.betdaylite.domain.usecases.DepositFundsUseCase
import com.albin.betdaylite.domain.usecases.GetFeaturedMatchUseCase
import com.albin.betdaylite.domain.usecases.GetMatchesUseCase
import com.albin.betdaylite.domain.usecases.GetWalletBalanceUseCase
import com.albin.betdaylite.domain.usecases.PlaceBetUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockGetMatchesUseCase: GetMatchesUseCase

    @Mock
    private lateinit var mockGetFeaturedMatchUseCase: GetFeaturedMatchUseCase

    @Mock
    private lateinit var mockPlaceBetUseCase: PlaceBetUseCase

    @Mock
    private lateinit var mockGetWalletBalanceUseCase: GetWalletBalanceUseCase

    @Mock
    private lateinit var mockDepositFundsUseCase: DepositFundsUseCase

    private lateinit var viewModel: HomeViewModel

    private val testMatch = Match(
        id = "m1",
        startTime = Instant.now(),
        league = League("pl", "Premier League", "England"),
        homeTeam = Team("tot", "Tottenham", "TOT"),
        awayTeam = Team("che", "Chelsea", "CHE"),
        market = Market("1X2", Odds(2.0, 3.0, 4.0))
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)

        whenever(mockGetMatchesUseCase(any())).thenReturn(flowOf(listOf(testMatch)))
        whenever(mockGetFeaturedMatchUseCase()).thenReturn(flowOf(testMatch))
        whenever(mockGetWalletBalanceUseCase()).thenReturn(
            flowOf(UserWallet(balance = 1250.0, totalWagered = 100.0, totalWon = 150.0, activeBetsCount = 1))
        )

        viewModel = HomeViewModel(
            getMatchesUseCase = mockGetMatchesUseCase,
            getFeaturedMatchUseCase = mockGetFeaturedMatchUseCase,
            placeBetUseCase = mockPlaceBetUseCase,
            getWalletBalanceUseCase = mockGetWalletBalanceUseCase,
            depositFundsUseCase = mockDepositFundsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_loadsMatchesAndWalletSuccessfully() = runTest(testDispatcher) {
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.matches.size)
        assertEquals("m1", state.matches.first().id)
        assertNotNull(state.featuredMatch)
        assertEquals(1250.0, state.wallet?.balance)
    }

    @Test
    fun onLeagueSelected_updatesSelectedLeague() = runTest(testDispatcher) {
        viewModel.onLeagueSelected("LaLiga")
        assertEquals("LaLiga", viewModel.uiState.value.selectedLeague)
    }
}
