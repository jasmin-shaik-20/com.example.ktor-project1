package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object LoginConfig {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val secret = config.property("ktor.jwt.secret").getString()
    val issuer = config.property("ktor.jwt.issuer").getString()
    val audience = config.property("ktor.jwt.audience").getString()
    val loginNameMinLength = config.property("ktor.LoginValidation.loginNameMinLength").getString().toIntOrNull()
    val loginNameMaxLength = config.property("ktor.LoginValidation.loginNameMaxLength").getString().toIntOrNull()
    val loginPasswordMinLength = config.property("ktor.LoginValidation.loginPasswordMinLength")
        .getString()?.toIntOrNull()
    val loginPasswordMaxLength = config.property("ktor.LoginValidation.loginPasswordMaxLength").
    getString()?.toIntOrNull()

}