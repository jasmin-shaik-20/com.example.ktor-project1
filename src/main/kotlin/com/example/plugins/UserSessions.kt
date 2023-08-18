package com.example.plugins

import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.SessionTransportTransformerEncrypt
import com.example.dao.UserSession
import io.ktor.util.hex
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureSessions() {
    val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
    val secretSignKey = hex("6819b57a326945c1968f45236589")
    val seconds=300
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = seconds.toLong()
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
}


