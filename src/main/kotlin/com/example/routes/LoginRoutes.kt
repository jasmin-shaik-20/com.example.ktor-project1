package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.Login
import com.example.endpoints.ApiEndPoint
import com.example.endpoints.ApiEndPoint.TOKEN_EXPIRATION
import com.example.services.LoginServices
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import java.util.Date

fun Application.configureLoginRoutes(){

    val config = HoconApplicationConfig(ConfigFactory.load())
    val secret = config.property("ktor.jwt.secret").getString()
    val issuer = config.property("ktor.jwt.issuer").getString()
    val audience = config.property("ktor.jwt.audience").getString()
    val loginNameMinLength= config.property("ktor.LoginValidation.loginNameMinLength").getString()?.toIntOrNull()
    val loginNameMaxLength= config.property("ktor.LoginValidation.loginNameMaxLength").getString()?.toIntOrNull()
    val loginPasswordMinLength= config.property("ktor.LoginValidation.loginPasswordMinLength").
    getString()?.toIntOrNull()
    val loginPasswordMaxLength= config.property("ktor.LoginValidation.loginPasswordMaxLength").
    getString()?.toIntOrNull()

    routing{
        route(ApiEndPoint.LOGIN){
            val loginServices:LoginServices by inject()
            post("/login"){
               loginServices.handleUserLogin(call,secret, issuer, audience, loginNameMinLength, loginNameMaxLength, loginPasswordMinLength, loginPasswordMaxLength)
            }

            authenticate("auth-jwt") {
                get("/hello") {
                    loginServices.handleAuthenticatedHello(call)
                }
            }
        }
    }
}
