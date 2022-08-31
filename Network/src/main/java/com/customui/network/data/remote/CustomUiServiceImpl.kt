package com.customui.network.data.remote

import com.customui.network.data.remote.dto.CustomUiResponse
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class CustomUiServiceImpl(
    private val client: HttpClient
) : CustomUiService {

    override suspend fun getCustomUi(): List<CustomUiResponse> {
        return try {
            client.get { url{HttpRoutes.POSTS} } as List<CustomUiResponse>
        } catch (e: RedirectResponseException) {
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }
//
//    override suspend fun createPost(postRequest: PostRequest): PostResponse? {
//        return try {
//            client.post<PostResponse> {
//                url(HttpRoutes.POSTS)
//                contentType(ContentType.Application.Json)
//                body = postRequest
//            }
//        } catch (e: RedirectResponseException) {
//            println("Error: ${e.response.status.description}")
//            null
//        } catch (e: ClientRequestException) {
//            println("Error: ${e.response.status.description}")
//            null
//        } catch (e: ServerResponseException) {
//            println("Error: ${e.response.status.description}")
//            null
//        } catch (e: Exception) {
//            println("Error: ${e.message}")
//            null
//        }
//    }
}