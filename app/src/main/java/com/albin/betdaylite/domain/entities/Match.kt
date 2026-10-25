package com.albin.betdaylite.domain.entities

import java.time.Instant

data class Match(
    val id: String,
    val startTime: Instant,
    val league: League,
    val homeTeam: Team,
    val awayTeam: Team,
    val market: Market,
    val isLive: Boolean = false,
    val liveMinute: Int? = null,
    val homeScore: Int? = null,
    val awayScore: Int? = null
)

data class League(
    val id: String,
    val name: String,
    val country: String,
    val emblemUrl: String? = null
)

data class Team(
    val id: String,
    val name: String,
    val shortName: String,
    val logoUrl: String? = null
)

data class Market(
    val type: String,
    val odds: Odds
)

data class Odds(
    val home: Double,
    val draw: Double,
    val away: Double
) {
    fun getOddForPick(pick: BetPick): Double {
        return when (pick) {
            BetPick.HOME -> home
            BetPick.DRAW -> draw
            BetPick.AWAY -> away
        }
    }
}
