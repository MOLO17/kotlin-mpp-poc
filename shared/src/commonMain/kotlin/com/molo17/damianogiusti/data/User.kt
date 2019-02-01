package com.molo17.damianogiusti.data

/**
 * Created by Damiano Giusti on 01/02/19.
 */
data class User(
    val id: String,
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val gender: Gender,
    val profilePictureUrl: String
) {
    enum class Gender {
        MALE, FEMALE
    }
}