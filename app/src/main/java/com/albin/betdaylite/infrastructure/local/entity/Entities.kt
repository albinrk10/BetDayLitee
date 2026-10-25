package com.albin.betdaylite.infrastructure.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches_table")
data class MatchEntity(
    @PrimaryKey
    val id: String,
    val startTime: Long,           // Unix timestamp en milisegundos
    val leagueId: String,
    val leagueName: String,
    val leagueCountry: String,
    val homeTeamId: String,
    val homeTeamName: String,
    val homeTeamShortName: String,
    val awayTeamId: String,
    val awayTeamName: String,
    val awayTeamShortName: String,
    val homeOdd: Double,
    val drawOdd: Double,
    val awayOdd: Double
)

@Entity(tableName = "bets_table")
data class BetEntity(
    @PrimaryKey
    val id: String,
    val matchId: String,
    val matchDescription: String,
    val leagueName: String,
    val pick: String,              // "HOME", "DRAW", "AWAY"
    val odd: Double,
    val stake: Double,
    val status: String,            // "PENDING", "WON", "LOST"
    val placedAt: Long,            // Unix timestamp en milisegundos
    val payout: Double?            // null si PENDING
)

@Entity(tableName = "wallet_table")
data class WalletEntity(
    @PrimaryKey
    val id: Int = 1,
    val balance: Double,
    val totalWagered: Double,
    val totalWon: Double
)
