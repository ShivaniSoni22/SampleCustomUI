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

    suspend fun getCustomUi(): CustomUiResponse

    companion object {
        fun create(): CustomUiService {
            return CustomUiServiceImpl(
                client = HttpClient(Android) {
                    // Logging
                    setUpLogging()
                    // JSON
                    setUpContentNegotiation()
                    // Timeout
                    setUpTimeout()
                    // Apply to all requests
                    setUpHeader()
                    //Observer
                    setUpObserver()
                }
            )
        }

        private fun HttpClientConfig<AndroidEngineConfig>.setUpContentNegotiation() {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                })
            }
        }

        private fun HttpClientConfig<AndroidEngineConfig>.setUpLogging() {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }
                }
                level = LogLevel.ALL
            }
        }

        private fun HttpClientConfig<AndroidEngineConfig>.setUpObserver() {
            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("HTTP status:", "${response.status.value}")
                }
            }
        }

        private fun HttpClientConfig<AndroidEngineConfig>.setUpHeader() {
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

        private fun HttpClientConfig<AndroidEngineConfig>.setUpTimeout() {
            install(HttpTimeout) {
                requestTimeoutMillis = 60000L
                connectTimeoutMillis = 60000L
                socketTimeoutMillis = 60000L
            }
        }
    }
}