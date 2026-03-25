package com.albin.betdaylite.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.albin.betdaylite.domain.model.Match
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.domain.usecase.GetMatchesGroupedByHourUseCase
import com.albin.betdaylite.domain.usecase.PlaceBetUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

/**
 * Test para HomeViewModel
 *
 * Razón: Unit test para presentación
 * SOLID: Mock use cases, verifica state management
 */

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var mockGetMatches: GetMatchesGroupedByHourUseCase
    
    @Mock
    private lateinit var mockPlaceBet: PlaceBetUseCase

    @Mock
    private lateinit var mockMatchRepository: MatchRepository

    @Mock
    private lateinit var mockBetRepository: BetRepository
    
    private lateinit var viewModel: HomeViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    /**
     * Test: ViewModel carga matches en init
     */
    @Test
    fun `viewModel should load matches on init`() = runTest {
        // Arrange
        val mockMatches = mapOf<Int, List<Match>>()
        whenever(mockGetMatches()).thenReturn(flowOf(mockMatches))
        whenever(mockMatchRepository.ensureMatchesBootstrapped()).thenReturn(Unit)
        whenever(mockBetRepository.ensureBetsBootstrapped()).thenReturn(Unit)

        viewModel = HomeViewModel(
            getMatchesGroupedByHourUseCase = mockGetMatches,
            placeBetUseCase = mockPlaceBet,
            matchRepository = mockMatchRepository,
            betRepository = mockBetRepository
        )
        
        // Act
        // Already happens in init() of HomeViewModel
        
        // Assert
        val state = viewModel.uiState.value
        assertTrue(
            "State should be Success or Loading after init",
            state is HomeUiState.Success || state is HomeUiState.Loading
        )
    }
}

