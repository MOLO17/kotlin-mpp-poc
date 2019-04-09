package com.molo17.damianogiusti.data

import com.molo17.damianogiusti.data.rest.AllUsersResponseDto
import com.molo17.damianogiusti.data.rest.SharedUsersApi
import com.molo17.damianogiusti.data.rest.UsersApi

/**
 * Created by Damiano Giusti on 01/02/19.
 */
class UsersRepository(private val usersApi: UsersApi = SharedUsersApi()) {

    suspend fun getAllUsers(): List<User> {
        val users = usersApi.getUsers()
        return users.results.map(::mapToUser)
    }
}

private fun mapToUser(result: AllUsersResponseDto.Result): User = User(
    id = result.login.uuid,
    name = result.name.first,
    surname = result.name.last,
    username = result.login.username,
    email = result.email,
    gender = if (result.gender == "female") User.Gender.FEMALE else User.Gender.MALE,
    profilePictureUrl = result.picture.large
)
