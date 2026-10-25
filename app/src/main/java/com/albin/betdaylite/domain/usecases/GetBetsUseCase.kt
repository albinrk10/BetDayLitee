package com.albin.betdaylite.domain.usecases

import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetStatus
import com.albin.betdaylite.domain.repository.BetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBetsUseCase @Inject constructor(
    private val betRepository: BetRepository
) {
    operator fun invoke(statusFilter: BetStatus? = null): Flow<List<Bet>> {
        return if (statusFilter == null) {
            betRepository.getAllBets()
        } else {
            betRepository.getBetsByStatus(statusFilter)
        }
    }
}
