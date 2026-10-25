package com.albin.betdaylite.infrastructure.remote.mapper

import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.BetStatus
import com.albin.betdaylite.domain.entities.League
import com.albin.betdaylite.domain.entities.Market
import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.entities.Odds
import com.albin.betdaylite.domain.entities.Team
import com.albin.betdaylite.infrastructure.local.entity.BetEntity
import com.albin.betdaylite.infrastructure.local.entity.MatchEntity
import com.albin.betdaylite.infrastructure.remote.dto.BetDto
import com.albin.betdaylite.infrastructure.remote.dto.MatchDto
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object MatchMapper {

    fun parseInstant(isoString: String): Instant {
        return try {
            Instant.parse(isoString)
        } catch (e: Exception) {
            try {
                OffsetDateTime.parse(isoString, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant()
            } catch (ex: Exception) {
                Instant.now()
            }
        }
    }

    fun toEntity(dto: MatchDto): MatchEntity {
        val startTimeMillis = parseInstant(dto.startTime).toEpochMilli()
        return MatchEntity(
            id = dto.id,
            startTime = startTimeMillis,
            leagueId = dto.league.id,
            leagueName = dto.league.name,
            leagueCountry = dto.league.country,
            homeTeamId = dto.homeTeam.id,
            homeTeamName = dto.homeTeam.name,
            homeTeamShortName = dto.homeTeam.shortName,
            awayTeamId = dto.awayTeam.id,
            awayTeamName = dto.awayTeam.name,
            awayTeamShortName = dto.awayTeam.shortName,
            homeOdd = dto.market.odds.home,
            drawOdd = dto.market.odds.draw,
            awayOdd = dto.market.odds.away
        )
    }

    fun toDomain(entity: MatchEntity): Match {
        return Match(
            id = entity.id,
            startTime = Instant.ofEpochMilli(entity.startTime),
            league = League(
                id = entity.leagueId,
                name = entity.leagueName,
                country = entity.leagueCountry
            ),
            homeTeam = Team(
                id = entity.homeTeamId,
                name = entity.homeTeamName,
                shortName = entity.homeTeamShortName
            ),
            awayTeam = Team(
                id = entity.awayTeamId,
                name = entity.awayTeamName,
                shortName = entity.awayTeamShortName
            ),
            market = Market(
                type = "1X2",
                odds = Odds(
                    home = entity.homeOdd,
                    draw = entity.drawOdd,
                    away = entity.awayOdd
                )
            ),
            isLive = false
        )
    }
}

object BetMapper {

    fun toEntity(dto: BetDto): BetEntity {
        val placedAtMillis = MatchMapper.parseInstant(dto.placedAt).toEpochMilli()
        return BetEntity(
            id = dto.id,
            matchId = dto.matchId,
            matchDescription = "Partido #${dto.matchId.takeLast(4)}",
            leagueName = "Liga",
            pick = dto.pick,
            odd = dto.odd,
            stake = dto.stake,
            status = dto.status,
            placedAt = placedAtMillis,
            payout = dto.returnValue
        )
    }

    fun toEntity(domain: Bet): BetEntity {
        return BetEntity(
            id = domain.id,
            matchId = domain.matchId,
            matchDescription = domain.matchDescription,
            leagueName = domain.leagueName,
            pick = domain.pick.name,
            odd = domain.odd,
            stake = domain.stake,
            status = domain.status.name,
            placedAt = domain.placedAt.toEpochMilli(),
            payout = domain.payout
        )
    }

    fun toDomain(entity: BetEntity): Bet {
        return Bet(
            id = entity.id,
            matchId = entity.matchId,
            matchDescription = entity.matchDescription,
            leagueName = entity.leagueName,
            pick = BetPick.fromString(entity.pick),
            odd = entity.odd,
            stake = entity.stake,
            status = BetStatus.fromString(entity.status),
            placedAt = Instant.ofEpochMilli(entity.placedAt),
            payout = entity.payout
        )
    }
}
