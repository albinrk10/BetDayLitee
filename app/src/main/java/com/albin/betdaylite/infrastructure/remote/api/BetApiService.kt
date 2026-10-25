package com.albin.betdaylite.infrastructure.remote.api

import com.albin.betdaylite.infrastructure.remote.dto.BetsResponse
import com.albin.betdaylite.infrastructure.remote.dto.MatchesResponse
import retrofit2.http.GET

interface BetApiService {
    @GET("matches.json")
    suspend fun getMatches(): MatchesResponse

    @GET("bets.json")
    suspend fun getBets(): BetsResponse
}
