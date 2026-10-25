package com.albin.betdaylite.di

import android.content.Context
import com.albin.betdaylite.infrastructure.remote.api.BetApiService
import com.albin.betdaylite.infrastructure.remote.interceptor.AssetJsonInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideAssetInterceptor(
        @ApplicationContext context: Context
    ): AssetJsonInterceptor = AssetJsonInterceptor(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        assetInterceptor: AssetJsonInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(assetInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://local.assets/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideBetApiService(retrofit: Retrofit): BetApiService =
        retrofit.create(BetApiService::class.java)
}
