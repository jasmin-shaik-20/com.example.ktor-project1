package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.Login
import com.example.endpoints.ApiEndPoint
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
    val tokenExpiration= 60000L

    routing{
        route(ApiEndPoint.LOGIN){
            post("/login"){
                val user = call.receive<Login>()
                if(user.username.length in loginNameMinLength!!..loginNameMaxLength!! &&
                    user.password.length in loginPasswordMinLength!!..loginPasswordMaxLength!!) {
                    if (user.username.isNotEmpty() && user.password.isNotEmpty()) {
                        val token = JWT.create()
                            .withAudience(audience)
                            .withIssuer(issuer)
                            .withClaim("username", user.username)
                            .withExpiresAt(Date(System.currentTimeMillis() + tokenExpiration))
                            .sign(Algorithm.HMAC256(secret))
                        call.respond(hashMapOf("token" to token))
                    } else {
                        call.respond("Missing Parameters")
                    }
                }
                else{
                    call.respond("Invalid length of username and password")
                }
            }

            authenticate("auth-jwt") {
                get("/hello") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
                }
            }
        }
    }
}
