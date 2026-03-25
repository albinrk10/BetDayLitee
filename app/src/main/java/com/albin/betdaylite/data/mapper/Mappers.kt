package com.albin.betdaylite.data.mapper

import com.albin.betdaylite.data.local.entity.BetEntity
import com.albin.betdaylite.data.local.entity.MatchEntity
import com.albin.betdaylite.data.remote.dto.BetDto
import com.albin.betdaylite.data.remote.dto.MatchDto
import com.albin.betdaylite.domain.model.Bet
import com.albin.betdaylite.domain.model.BetPick
import com.albin.betdaylite.domain.model.BetStatus
import com.albin.betdaylite.domain.model.League
import com.albin.betdaylite.domain.model.Match
import com.albin.betdaylite.domain.model.Market
import com.albin.betdaylite.domain.model.Odds
import com.albin.betdaylite.domain.model.Team
import java.time.Instant


class BetMapper {
    

    fun toDomain(dto: BetDto): Bet {
        return Bet(
            id = dto.id,
            matchId = dto.matchId,
            pick = BetPick.valueOf(dto.pick),
            odd = dto.odd,
            stake = dto.stake,
            status = BetStatus.valueOf(dto.status),
            placedAt = Instant.parse(dto.placedAt),
            payout = dto.returnValue
        )
    }
    

    fun toDomain(entity: BetEntity): Bet {
        return Bet(
            id = entity.id,
            matchId = entity.matchId,
            pick = BetPick.valueOf(entity.pick),
            odd = entity.odd,
            stake = entity.stake,
            status = BetStatus.valueOf(entity.status),
            placedAt = Instant.ofEpochMilli(entity.placedAt),
            payout = entity.returnValue
        )
    }
    
    /**
     * Convierte Bet (Domain) → BetEntity (Room)
     * 
     * Para persistencia local
     */
    fun toEntity(domain: Bet): BetEntity {
        return BetEntity(
            id = domain.id,
            matchId = domain.matchId,
            pick = domain.pick.name,
            odd = domain.odd,
            stake = domain.stake,
            status = domain.status.name,
            placedAt = domain.placedAt.toEpochMilli(),
            returnValue = domain.payout
        )
    }
}

class MatchMapper {
    
    /**
     * Convierte MatchDto (de API/JSON) → Match (Domain)
     */
    fun toDomain(dto: MatchDto): Match {
        return Match(
            id = dto.id,
            startTime = Instant.parse(dto.startTime),
            league = League(
                id = dto.league.id,
                name = dto.league.name,
                country = dto.league.country
            ),
            homeTeam = Team(
                id = dto.homeTeam.id,
                name = dto.homeTeam.name,
                shortName = dto.homeTeam.shortName
            ),
            awayTeam = Team(
                id = dto.awayTeam.id,
                name = dto.awayTeam.name,
                shortName = dto.awayTeam.shortName
            ),
            market = Market(
                type = dto.market.type,
                odds = Odds(
                    home = dto.market.odds.home,
                    draw = dto.market.odds.draw,
                    away = dto.market.odds.away
                )
            )
        )
    }
    
    /**
     * Convierte MatchEntity (de Room) → Match (Domain)
     */
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
            )
        )
    }
    
    /**
     * Convierte Match (Domain) → MatchEntity (Room)
     */
    fun toEntity(domain: Match): MatchEntity {
        return MatchEntity(
            id = domain.id,
            startTime = domain.startTime.toEpochMilli(),
            leagueId = domain.league.id,
            leagueName = domain.league.name,
            leagueCountry = domain.league.country,
            homeTeamId = domain.homeTeam.id,
            homeTeamName = domain.homeTeam.name,
            homeTeamShortName = domain.homeTeam.shortName,
            awayTeamId = domain.awayTeam.id,
            awayTeamName = domain.awayTeam.name,
            awayTeamShortName = domain.awayTeam.shortName,
            homeOdd = domain.market.odds.home,
            drawOdd = domain.market.odds.draw,
            awayOdd = domain.market.odds.away
        )
    }
}

