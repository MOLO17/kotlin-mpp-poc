package com.molo17.damianogiusti.data.rest

/**
 * Created by Damiano Giusti on 01/02/19.
 */

interface UsersApi {

    suspend fun getUsers(): AllUsersResponseDto
}