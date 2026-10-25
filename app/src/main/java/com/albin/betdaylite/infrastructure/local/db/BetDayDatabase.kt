package com.albin.betdaylite.infrastructure.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.albin.betdaylite.infrastructure.local.dao.BetDao
import com.albin.betdaylite.infrastructure.local.dao.MatchDao
import com.albin.betdaylite.infrastructure.local.dao.WalletDao
import com.albin.betdaylite.infrastructure.local.entity.BetEntity
import com.albin.betdaylite.infrastructure.local.entity.MatchEntity
import com.albin.betdaylite.infrastructure.local.entity.WalletEntity

@Database(
    entities = [MatchEntity::class, BetEntity::class, WalletEntity::class],
    version = 2,
    exportSchema = false
)
abstract class BetDayDatabase : RoomDatabase() {

    abstract fun matchDao(): MatchDao
    abstract fun betDao(): BetDao
    abstract fun walletDao(): WalletDao

    companion object {
        @Volatile
        private var instance: BetDayDatabase? = null

        fun getInstance(context: Context): BetDayDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BetDayDatabase::class.java,
                    "betday_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
            }
        }
    }
}
