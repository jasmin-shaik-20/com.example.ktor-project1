package com.example.routes

import com.example.dao.RedisUtils
import com.example.dao.UserSession
import com.example.endpoints.ApiEndPoint
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.get
import io.ktor.server.sessions.set

fun Application.configureUserSession() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val sessionNameMinLength= config.property("ktor.SessionValidation.sessionNameMinLength").getString()?.toIntOrNull()
    val sessionNameMaxLength= config.property("ktor.SessionValidation.sessionNameMaxLength").getString()?.toIntOrNull()
    val sessionPasswordMinLength= config.property("ktor.SessionValidation.sessionPasswordMinLength").getString()?.toIntOrNull()
    val sessionPasswordMaxLength= config.property("ktor.SessionValidation.sessionPasswordMaxLength").getString()?.toIntOrNull()
    routing {
        route(ApiEndPoint.SESSION) {
            get("/login") {
                val userSession = UserSession(id = "2", username = "Jasmin", password = "Jas@20")
                if(userSession.username.length in sessionNameMinLength!!..sessionNameMaxLength!! && userSession.password.length in sessionPasswordMinLength!!..sessionPasswordMaxLength!!) {
                    call.sessions.set(userSession)
                    RedisUtils.setWithExpiration(userSession.id, 300, userSession.toJson())
                    call.respond("login Successfully")
                    call.respondRedirect("/user-session")
                }
                else{
                    call.respond("Invalid length of username and password")
                }
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