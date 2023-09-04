package com.example.plugins

import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.SessionTransportTransformerEncrypt
import com.example.database.table.UserSession
import com.example.utils.appConstants.GlobalConstants.COOKIE_PATH
import com.example.utils.appConstants.GlobalConstants.SECONDS
import com.example.utils.appConstants.GlobalConstants.SECRET_ENCRYPT_KEY
import com.example.utils.appConstants.GlobalConstants.SECRET_SIGN_KEY
import io.ktor.util.hex
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureSessions() {
    val secretEncryptKey = SECRET_ENCRYPT_KEY
    val secretSignKey = SECRET_SIGN_KEY
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = COOKIE_PATH
            cookie.maxAgeInSeconds = SECONDS
            transform(SessionTransportTransformerEncrypt(hex(secretEncryptKey), hex(secretSignKey)))
        }
    }
}


