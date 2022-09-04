package com.customui.network.data.remote

import com.customui.network.data.remote.dto.CustomUiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class CustomUiServiceImpl(
    private val client: HttpClient
) : CustomUiService {

    override suspend fun getCustomUi(): CustomUiResponse {
        return try {
            client.get {
                url("https://demo.ezetap.com/mobileapps/android_assignment.json")
            }.body()
        } catch (e: RedirectResponseException) {
            println("Error: ${e.response.status.description}")
            CustomUiResponse()
        } catch (e: ClientRequestException) {
            println("Error: ${e.response.status.description}")
            CustomUiResponse()
        } catch (e: ServerResponseException) {
            println("Error: ${e.response.status.description}")
            CustomUiResponse()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            CustomUiResponse()
        }
    }

}