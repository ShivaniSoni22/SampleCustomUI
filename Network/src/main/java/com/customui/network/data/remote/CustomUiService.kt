package com.customui.network.data.remote

import android.util.Log
import com.customui.network.data.remote.dto.CustomUiResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface CustomUiService {
    suspend fun getCustomUi(): List<CustomUiResponse>
//    suspend fun createPost(postRequest: PostRequest): PostResponse?

    companion object {
        fun create(): CustomUiService {
            return CustomUiServiceImpl(
                client = HttpClient(Android) {
                    // Logging
                    install(Logging) {
                        logger = object : Logger {
                            override fun log(message: String) {
                                Log.v("Logger Ktor =>", message)
                            }
                        }
                        level = LogLevel.ALL
                    }
                    // JSON
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                            encodeDefaults = false
                        })
                    }
                    // Timeout
                    install(HttpTimeout) {
                        requestTimeoutMillis = 30000L
                        connectTimeoutMillis = 30000L
                        socketTimeoutMillis = 30000L
                    }
                    // Apply to all requests
                    install(DefaultRequest) {
                        header(HttpHeaders.ContentType, ContentType.Application.Json)
                    }
                    install(ResponseObserver) {
                        onResponse { response ->
                            Log.d("HTTP status:", "${response.status.value}")
                        }
                    }
                }
            )
        }
    }
}