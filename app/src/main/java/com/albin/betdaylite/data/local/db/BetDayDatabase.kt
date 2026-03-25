package com.albin.betdaylite.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.albin.betdaylite.data.local.dao.BetDao
import com.albin.betdaylite.data.local.dao.MatchDao
import com.albin.betdaylite.data.local.entity.BetEntity
import com.albin.betdaylite.data.local.entity.MatchEntity



@Database(
    entities = [BetEntity::class, MatchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BetDayDatabase : RoomDatabase() {
    
    abstract fun betDao(): BetDao
    abstract fun matchDao(): MatchDao
    
    companion object {
        @Volatile
        private var instance: BetDayDatabase? = null
        

        fun getInstance(context: Context): BetDayDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BetDayDatabase::class.java,
                    "betday.db"
                )
                    .fallbackToDestructiveMigration()  // Dev: borrar BD en cambios de schema
                    .build()
                    .also { instance = it }
            }
        }
    }
}

