package com.albin.betdaylite.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.albin.betdaylite.data.local.entity.BetEntity
import com.albin.betdaylite.data.local.entity.MatchEntity
import kotlinx.coroutines.flow.Flow



@Dao
interface BetDao {
    

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBet(bet: BetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBets(bets: List<BetEntity>)

    @Query("SELECT COUNT(*) FROM bets_table")
    suspend fun getCount(): Int
    

    @Query("SELECT * FROM bets_table ORDER BY placedAt DESC")
    fun getAllBets(): Flow<List<BetEntity>>
    

    @Query("SELECT * FROM bets_table WHERE status = :status ORDER BY placedAt DESC")
    fun getBetsByStatus(status: String): Flow<List<BetEntity>>

    @Query("SELECT * FROM bets_table WHERE id = :betId")
    suspend fun getBetById(betId: String): BetEntity?
}

@Dao
interface MatchDao {
    

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Query("SELECT COUNT(*) FROM matches_table")
    suspend fun getCount(): Int
    

    @Query("SELECT * FROM matches_table ORDER BY startTime ASC")
    fun getAllMatches(): Flow<List<MatchEntity>>
    

    @Query("SELECT * FROM matches_table WHERE id = :matchId")
    suspend fun getMatchById(matchId: String): MatchEntity?
}

