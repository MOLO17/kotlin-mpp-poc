package com.molo17.damianogiusti.data.rest

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class SharedUsersApi : UsersApi {

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(AllUsersResponseDto::class, AllUsersResponseDto.serializer())
            }
        }
    }

    override suspend fun getUsers(): AllUsersResponseDto =
        httpClient.get("https://randomuser.me/api/?results=50")
}