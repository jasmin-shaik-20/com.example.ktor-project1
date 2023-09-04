package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object UserProfileConfig {

    val config = HoconApplicationConfig(ConfigFactory.load())
    val emailMinLength = config.property("ktor.ProfileValidation.emailMinLength").getString()?.toIntOrNull()
    val emailMaxLength = config.property("ktor.ProfileValidation.emailMaxLength").getString()?.toIntOrNull()
}