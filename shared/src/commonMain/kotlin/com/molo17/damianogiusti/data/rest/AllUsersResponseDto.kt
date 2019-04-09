package com.molo17.damianogiusti.data.rest

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersResponseDto(
    @SerialName("info")
    val info: Info,

    @SerialName("results")
    val results: List<Result>
) {
    @Serializable
    data class Result(
        @SerialName("cell")
        val cell: String,

        @SerialName("dob")
        val dob: Dob,

        @SerialName("email")
        val email: String,

        @SerialName("gender")
        val gender: String,

        @SerialName("id")
        val id: Id,

        @SerialName("location")
        val location: Location,

        @SerialName("login")
        val login: Login,

        @SerialName("name")
        val name: Name,

        @SerialName("nat")
        val nat: String,

        @SerialName("phone")
        val phone: String,

        @SerialName("picture")
        val picture: Picture,

        @SerialName("registered")
        val registered: Registered
    ) {
        @Serializable
        data class Location(

            @SerialName("city")
            val city: String,

            @SerialName("coordinates")
            val coordinates: Coordinates,

            @SerialName("postcode")
            val postcode: String,

            @SerialName("state")
            val state: String,

            @SerialName("street")
            val street: String,

            @SerialName("timezone")
            val timezone: Timezone
        ) {
            @Serializable
            data class Coordinates(

                @SerialName("latitude")
                val latitude: String,

                @SerialName("longitude")
                val longitude: String
            )

            @Serializable
            data class Timezone(

                @SerialName("description")
                val description: String,

                @SerialName("offset")
                val offset: String
            )
        }

        @Serializable
        data class Picture(

            @SerialName("large")
            val large: String,

            @SerialName("medium")
            val medium: String,

            @SerialName("thumbnail")
            val thumbnail: String
        )

        @Serializable
        data class Registered(

            @SerialName("age")
            val age: Int,

            @SerialName("date")
            val date: String
        )

        @Serializable
        data class Id(

            @SerialName("name")
            val name: String,

            @Optional
            @SerialName("value")
            val value: String? = null
        )

        @Serializable
        data class Name(

            @SerialName("first")
            val first: String,

            @SerialName("last")
            val last: String,

            @SerialName("title")
            val title: String
        )

        @Serializable
        data class Login(

            @SerialName("md5")
            val md5: String,

            @SerialName("password")
            val password: String,

            @SerialName("salt")
            val salt: String,

            @SerialName("sha1")
            val sha1: String,

            @SerialName("sha256")
            val sha256: String,

            @SerialName("username")
            val username: String,

            @SerialName("uuid")
            val uuid: String
        )

        @Serializable
        data class Dob(

            @SerialName("age")
            val age: Int,

            @SerialName("date")
            val date: String
        )
    }

    @Serializable
    data class Info(

        @SerialName("page")
        val page: Int,

        @SerialName("results")
        val results: Int,

        @SerialName("seed")
        val seed: String,

        @SerialName("version")
        val version: String
    )
}