package com.albin.betdaylite.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bets_table")
data class BetEntity(
    @PrimaryKey
    val id: String,
    val matchId: String,
    val pick: String,              // Stored as string ("HOME", "AWAY", "DRAW")
    val odd: Double,
    val stake: Double,
    val status: String,            // Stored as string ("PENDING", "WON", "LOST")
    val placedAt: Long,            // Unix timestamp en milisegundos
    val returnValue: Double?       // null si PENDING
)

@Entity(tableName = "matches_table")
data class MatchEntity(
    @PrimaryKey
    val id: String,
    val startTime: Long,           // Unix timestamp
    val leagueId: String,
    val leagueName: String,
    val leagueCountry: String,
    val homeTeamId: String,
    val homeTeamName: String,
    val homeTeamShortName: String,
    val awayTeamId: String,
    val awayTeamName: String,
    val awayTeamShortName: String,
    val homeOdd: Double,           // Cuota 1
    val drawOdd: Double,           // Cuota X
    val awayOdd: Double            // Cuota 2
)

