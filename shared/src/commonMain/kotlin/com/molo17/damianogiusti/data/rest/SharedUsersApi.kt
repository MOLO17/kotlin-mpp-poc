package com.molo17.damianogiusti.data.rest

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class SharedUsersApi : UsersApi {

    private val httpClient = HttpClient()

    override suspend fun getUsers(): AllUsersResponseDto {
        val json = httpClient.get<String>("https://randomuser.me/api/?results=20")
        return Json.parse(AllUsersResponseDto.serializer(), json)
    }
}