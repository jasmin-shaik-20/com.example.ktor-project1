package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object UserConfig {

    val config = HoconApplicationConfig(ConfigFactory.load())
    val nameMinLength = config.property("ktor.UserValidation.nameMinLength").getString().toIntOrNull()
    val nameMaxLength = config.property("ktor.UserValidation.nameMaxLength").getString()?.toIntOrNull()

}