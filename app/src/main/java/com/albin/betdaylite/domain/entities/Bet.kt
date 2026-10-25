package com.albin.betdaylite.domain.entities

import java.time.Instant

data class Bet(
    val id: String,
    val matchId: String,
    val matchDescription: String,
    val leagueName: String,
    val pick: BetPick,
    val odd: Double,
    val stake: Double,
    val status: BetStatus,
    val placedAt: Instant,
    val payout: Double?
) {
    val potentialPayout: Double
        get() = stake * odd
}

data class BetSlip(
    val match: Match,
    val pick: BetPick,
    val odd: Double,
    val stake: Double = 10.0
) {
    val potentialPayout: Double
        get() = stake * odd
}

data class UserWallet(
    val balance: Double,
    val totalWagered: Double,
    val totalWon: Double,
    val activeBetsCount: Int
) {
    val profit: Double
        get() = totalWon - totalWagered
}
