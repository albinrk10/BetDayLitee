package com.albin.betdaylite.di

import android.content.Context
import com.albin.betdaylite.data.local.dao.BetDao
import com.albin.betdaylite.data.local.dao.MatchDao
import com.albin.betdaylite.data.local.db.BetDayDatabase
import com.albin.betdaylite.data.mapper.BetMapper
import com.albin.betdaylite.data.mapper.MatchMapper
import com.albin.betdaylite.data.remote.api.BetApi
import com.albin.betdaylite.data.repository.BetRepositoryImpl
import com.albin.betdaylite.data.repository.MatchRepositoryImpl
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Hilt Modules
 *
 * Razón: DIP - Dependency Inversion Principle
 * - Centralizamos creación de dependencias
 * - ViewModels no conocen implementaciones
 * - Testing: injectamos mocks fácilmente
 *
 * Pattern: Factory Pattern (Hilt actúa como factory)
 * Clean Code: Provider functions claramente nombradas
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideBetDayDatabase(
        @ApplicationContext context: Context
    ): BetDayDatabase = BetDayDatabase.getInstance(context)
    
    @Provides
    @Singleton
    fun provideBetDao(database: BetDayDatabase): BetDao = database.betDao()
    
    @Provides
    @Singleton
    fun provideMatchDao(database: BetDayDatabase): MatchDao = database.matchDao()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Provides
    @Singleton
    fun provideAssetInterceptor(
        @ApplicationContext context: Context
    ): Interceptor = Interceptor { chain ->
        val requestedFile = chain.request().url.encodedPath.removePrefix("/")
        if (requestedFile == "matches.json" || requestedFile == "bets.json") {
            val json = context.assets.open(requestedFile).bufferedReader().use { it.readText() }
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        } else {
            chain.proceed(chain.request())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(assetInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(assetInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit = 
        Retrofit.Builder()
            // Retrofit exige esquema http/https. Los JSON locales se sirven con el interceptor.
            .baseUrl("https://local.assets/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    
    @Provides
    @Singleton
    fun provideBetApi(retrofit: Retrofit): BetApi = retrofit.create(BetApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    
    @Provides
    @Singleton
    fun provideBetMapper(): BetMapper = BetMapper()
    
    @Provides
    @Singleton
    fun provideMatchMapper(): MatchMapper = MatchMapper()
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideBetRepository(
        betDao: BetDao,
        betMapper: BetMapper,
        betApi: BetApi
    ): BetRepository = BetRepositoryImpl(betDao, betMapper, betApi)
    
    @Provides
    @Singleton
    fun provideMatchRepository(
        matchDao: MatchDao,
        matchMapper: MatchMapper,
        betApi: BetApi
    ): MatchRepository = MatchRepositoryImpl(matchDao, matchMapper, betApi)
}

