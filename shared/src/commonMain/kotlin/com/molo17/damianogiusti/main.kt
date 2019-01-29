package com.molo17.damianogiusti

expect fun platformName(): String

fun getMessage() = "\"Parliamo di Koltin (soprattutto di Koltin per ${platformName()})" +
        " ma senza dimenticare altri sviluppi\" cit."