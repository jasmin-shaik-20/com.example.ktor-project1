package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.response.respond

fun Application.configureLogin() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val secret = config.property("ktor.jwt.secret").getString()
    val issuer = config.property("ktor.jwt.issuer").getString()
    val audience = config.property("ktor.jwt.audience").getString()
    val myRealm= config.property("ktor.jwt.myRealm").getString()
    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )

            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
