package com.albin.betdaylite.domain.model


import java.time.Instant


data class Match(
    val id: String,
    val startTime: Instant,
    val league: League,
    val homeTeam: Team,
    val awayTeam: Team,
    val market: Market
)

data class League(
    val id: String,
    val name: String,
    val country: String
)

data class Team(
    val id: String,
    val name: String,
    val shortName: String
)

data class Market(
    val type: String,  // "1X2"
    val odds: Odds
)

data class Odds(
    val home: Double,
    val draw: Double,
    val away: Double
)

