package com.albin.betdaylite.domain.usecases

import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.BetStatus
import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.WalletRepository
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class PlaceBetUseCase @Inject constructor(
    private val betRepository: BetRepository,
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(
        match: Match,
        pick: BetPick,
        odd: Double,
        stake: Double
    ): Result<Bet> {
        if (stake <= 0) {
            return Result.failure(IllegalArgumentException("El monto a apostar debe ser mayor a 0"))
        }

        val wallet = walletRepository.getWallet().first()
        if (wallet.balance < stake) {
            return Result.failure(IllegalStateException("Saldo insuficiente en tu billetera"))
        }

        val bet = Bet(
            id = "bet_${UUID.randomUUID().toString().take(8)}",
            matchId = match.id,
            matchDescription = "${match.homeTeam.name} vs ${match.awayTeam.name}",
            leagueName = match.league.name,
            pick = pick,
            odd = odd,
            stake = stake,
            status = BetStatus.PENDING,
            placedAt = Instant.now(),
            payout = null
        )

        // Descontar saldo y guardar apuesta
        walletRepository.deductBalance(stake)
        val placeResult = betRepository.placeBet(bet)
        return if (placeResult.isSuccess) {
            Result.success(bet)
        } else {
            Result.failure(placeResult.exceptionOrNull() ?: Exception("Error al guardar apuesta"))
        }
    }
}
