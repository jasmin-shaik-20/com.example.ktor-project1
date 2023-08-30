package com.example.services

import com.example.dao.RedisUtils
import com.example.dao.UserSession
import com.example.endpoints.ApiEndPoint.EXPIRE_TIME
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class UserSessionServices {
    suspend fun handleLogin(call:ApplicationCall,
        sessionNameMinLength: Int?,
        sessionNameMaxLength: Int?,
        sessionPasswordMinLength: Int?,
        sessionPasswordMaxLength: Int?
    ) {
        val userSession = UserSession(id = "2", username = "Jasmin", password = "Jas@20")
        if (userSession.username.length in sessionNameMinLength!!..sessionNameMaxLength!! &&
            userSession.password.length in sessionPasswordMinLength!!..sessionPasswordMaxLength!!
        ) {
            call.sessions.set(userSession)
            RedisUtils.setWithExpiration(userSession.id, EXPIRE_TIME, userSession.toJson())
            call.respond("login Successfully")
            call.respondRedirect("/user-session")
        } else {
            call.respond("Invalid length of username and password")
        }
    }

    suspend fun handleUserSession(call: ApplicationCall) {
        val sessionId = call.sessions.get<UserSession>()?.id ?: ""
        val userSessionJson = RedisUtils.get(sessionId)
        if (userSessionJson != null) {
            val userSession = UserSession.fromJson(userSessionJson)
            call.respondText("Username is ${userSession.username}")
        } else {
            call.respondText("Session doesn't exist or has expired.")
        }
    }

    suspend fun handleLogout(call: ApplicationCall) {
        val sessionId = call.sessions.get<UserSession>()?.id ?: ""
        RedisUtils.delete(sessionId)
        call.respondText("Logout successful!")
    }
}