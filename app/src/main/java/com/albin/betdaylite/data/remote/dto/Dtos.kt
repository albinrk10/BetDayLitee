package com.albin.betdaylite.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class MatchesResponse(
    val date: String,
    val timezone: String,
    val matches: List<MatchDto>
)

@Serializable
data class MatchDto(
    val id: String,
    val startTime: String,      // ISO 8601 format
    val league: LeagueDto,
    val homeTeam: TeamDto,
    val awayTeam: TeamDto,
    val market: MarketDto
)

@Serializable
data class LeagueDto(
    val id: String,
    val name: String,
    val country: String
)

@Serializable
data class TeamDto(
    val id: String,
    val name: String,
    val shortName: String
)

@Serializable
data class MarketDto(
    val type: String,
    val odds: OddsDto
)

@Serializable
data class OddsDto(
    val home: Double,
    val draw: Double,
    val away: Double
)

@Serializable
data class BetsResponse(
    val bets: List<BetDto>
)

@Serializable
data class BetDto(
    val id: String,
    val matchId: String,
    val placedAt: String,       // ISO 8601 format
    val pick: String,            // "HOME", "AWAY", "DRAW"
    val odd: Double,
    val stake: Double,
    val status: String,          // "PENDING", "WON", "LOST"
    @SerialName("return")
    val returnValue: Double?     // null si PENDING
)

