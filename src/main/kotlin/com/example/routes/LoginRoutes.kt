package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.Login
import io.ktor.http.HttpHeaders.Date
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureLoginRoutes(){
    val secret = "secret"
    val issuer = "http://0.0.0.0:8080/"
    val audience = "http://0.0.0.0:8080/hello"
    routing{
        route("/user login"){
            post("/login"){
                val user = call.receive<Login>()
                if(user.username.isNotEmpty() && user.password.isNotEmpty() ) {
                    val token = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("username", user.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                        .sign(Algorithm.HMAC256(secret))
                    call.respond(hashMapOf("token" to token))
                }
                else{
                    call.respond("Missing Parameters")
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