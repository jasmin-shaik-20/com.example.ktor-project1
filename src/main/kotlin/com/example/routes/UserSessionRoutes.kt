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
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.get
import io.ktor.server.sessions.set
import org.koin.ktor.ext.inject

fun Application.configureUserSession() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val sessionNameMinLength= config.property("ktor.SessionValidation.sessionNameMinLength").getString()?.toIntOrNull()
    val sessionNameMaxLength= config.property("ktor.SessionValidation.sessionNameMaxLength").getString()?.toIntOrNull()
    val sessionPasswordMinLength= config.property("ktor.SessionValidation.sessionPasswordMinLength").
    getString()?.toIntOrNull()
    val sessionPasswordMaxLength= config.property("ktor.SessionValidation.sessionPasswordMaxLength").
    getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.SESSION) {
            val userSessionServices=UserSessionServices()

            get("/login") {
                userSessionServices.handleLogin(call,sessionNameMinLength,sessionNameMaxLength,sessionPasswordMinLength,sessionPasswordMaxLength)
            }

            get("/userSession") {
                userSessionServices.handleUserSession(call)
            }
            get("/logout") {
                userSessionServices.handleLogout(call)
            }
        }
    }
}
