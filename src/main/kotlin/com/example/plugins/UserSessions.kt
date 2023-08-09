package com.example.plugins

import io.ktor.server.sessions.*
import com.example.dao.UserSession
import io.ktor.util.*
import io.ktor.server.application.*

fun Application.configureSessions() {
    val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
    val secretSignKey = hex("6819b57a326945c1968f45236589")
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 300
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
}

