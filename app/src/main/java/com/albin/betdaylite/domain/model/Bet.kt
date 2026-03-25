package com.albin.betdaylite.domain.model


import java.time.Instant



data class Bet(
    val id: String,
    val matchId: String,
    val pick: BetPick,          // HOME, DRAW, AWAY
    val odd: Double,
    val stake: Double,          // Dinero apostado
    val status: BetStatus,      // PENDING, WON, LOST
    val placedAt: Instant,
    val payout: Double?         // null si PENDING, monto retornado si WON/LOST
)


enum class BetPick {
    HOME,   // 1 - Gana el local
    DRAW,   // X - Empate
    AWAY    // 2 - Gana el visitante
}

enum class BetStatus {
    PENDING,
    WON,
    LOST
}

