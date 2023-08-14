package com.example.routes

import com.example.dao.RedisUtils
import com.example.dao.UserSession
import com.example.endpoints.ApiEndPoint
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureUserSession() {
    routing {
        route(ApiEndPoint.SESSION) {
            get("/login") {
                val userSession = UserSession(id = "2", username = "Jasmin", password = "Jas@20")
                call.sessions.set(userSession)
                RedisUtils.setWithExpiration(userSession.id, 300,userSession.toJson())
                call.respond("login Successfully")
                call.respondRedirect("/user-session")
            }
            get("/user-session") {
                val sessionId = call.sessions.get<UserSession>()?.id ?: ""
                val userSessionJson = RedisUtils.get(sessionId)
                if (userSessionJson != null) {
                    val userSession = UserSession.fromJson(userSessionJson)
                    call.respondText("Username is ${userSession.username}")
                } else {
                    call.respondText("Session doesn't exist or has expired.")
                }
            }
            get("/logout") {
                val sessionId = call.sessions.get<UserSession>()?.id ?: ""
                RedisUtils.delete(sessionId)
                call.respondText("Logout successful!")
            }
        }
    }
}