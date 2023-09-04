package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object CustomerConfig {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val customerNameMinLength = config.property("ktor.CustomerValidation.customerNameMinLength").getString()?.toIntOrNull()
    val customerNameMaxLength = config.property("ktor.CustomerValidation.customerNameMaxLength").getString()?.toIntOrNull()
}