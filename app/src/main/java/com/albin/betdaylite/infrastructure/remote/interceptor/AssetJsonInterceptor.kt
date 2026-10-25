package com.albin.betdaylite.infrastructure.remote.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AssetJsonInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath.removePrefix("/")
        
        if (path == "matches.json" || path == "bets.json") {
            return try {
                val jsonString = context.assets.open(path).bufferedReader().use { it.readText() }
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body(jsonString.toResponseBody("application/json".toMediaTypeOrNull()))
                    .build()
            } catch (e: Exception) {
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(500)
                    .message("Error loading asset: ${e.message}")
                    .body("{}".toResponseBody("application/json".toMediaTypeOrNull()))
                    .build()
            }
        }
        return chain.proceed(request)
    }
}
