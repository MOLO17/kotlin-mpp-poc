package com.molo17.damianogiusti.data.rest

import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class SharedUsersApi : UsersApi {

    private val httpClient = HttpClient()

    override suspend fun getUsers(): AllUsersResponseDto {
        return httpClient.get("https://randomuser.me/api/?results=20")
    }
}