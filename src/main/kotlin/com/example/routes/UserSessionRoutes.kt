package com.example.routes

import com.example.config.SessionsConfig.sessionNameMaxLength
import com.example.config.SessionsConfig.sessionNameMinLength
import com.example.config.SessionsConfig.sessionPasswordMaxLength
import com.example.config.SessionsConfig.sessionPasswordMinLength
import com.example.database.table.RedisUtils
import com.example.database.table.UserSession
import com.example.services.UserSessionServices
import com.example.utils.appConstants.ApiEndPoints
import com.example.utils.appConstants.GlobalConstants.EXPIRE_TIME
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.sessions.*

fun Application.configureUserSession() {

    routing {
        route(ApiEndPoints.SESSION) {

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
