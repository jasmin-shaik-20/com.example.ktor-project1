package com.example.routes

import com.example.dao.RedisUtils
import com.example.dao.UserSession
import com.example.endpoints.ApiEndPoint
import com.example.endpoints.ApiEndPoint.EXPIRE_TIME
import com.example.services.UserSessionServices
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.sessions.*

fun Application.configureUserSession() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val sessionNameMinLength= config.property("ktor.SessionValidation.sessionNameMinLength").getString().toIntOrNull()
    val sessionNameMaxLength= config.property("ktor.SessionValidation.sessionNameMaxLength").getString().toIntOrNull()
    val sessionPasswordMinLength= config.property("ktor.SessionValidation.sessionPasswordMinLength").
    getString().toIntOrNull()
    val sessionPasswordMaxLength= config.property("ktor.SessionValidation.sessionPasswordMaxLength").
    getString().toIntOrNull()

    routing {
        route(ApiEndPoint.SESSION) {
            val userSessionServices=UserSessionServices()

            get("/login") {
                val loginResult = userSessionServices.handleLogin(
                    sessionNameMinLength, sessionNameMaxLength, sessionPasswordMinLength, sessionPasswordMaxLength)
                when (loginResult) {
                    is LoginResult.Success -> {
                        call.sessions.set(loginResult.userSession)
                        RedisUtils.setWithExpiration(
                            loginResult.userSession.id, EXPIRE_TIME, loginResult.userSession.toJson())
                        call.respond("Login Successfully")
                        call.respondRedirect("/user-session")
                    }
                    is LoginResult.Error -> {
                        call.respond(loginResult.message)
                    }
                }
            }
            get("/user-session") {
                val sessionId = call.sessions.get<UserSession>()?.id ?: ""
                val response = userSessionServices.handleUserSession(sessionId)
                call.respondText(response)
            }

            get("/logout") {
                val sessionId = call.sessions.get<UserSession>()?.id ?: ""
                val response = userSessionServices.handleLogout(sessionId)
                call.respondText(response)
            }

        }
    }
}
sealed class LoginResult {
    data class Success(val userSession: UserSession) : LoginResult()
    data class Error(val message: String) : LoginResult()
}
