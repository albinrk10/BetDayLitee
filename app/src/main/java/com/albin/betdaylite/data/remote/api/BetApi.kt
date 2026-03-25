package com.albin.betdaylite.data.remote.api

import com.albin.betdaylite.data.remote.dto.BetsResponse
import com.albin.betdaylite.data.remote.dto.MatchesResponse
import retrofit2.http.GET



interface BetApi {
    

    @GET("matches.json")
    suspend fun getMatches(): MatchesResponse
    

    @GET("bets.json")
    suspend fun getUserBets(): BetsResponse
}

