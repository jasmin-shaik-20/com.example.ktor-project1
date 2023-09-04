package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object ProductConfig {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val productNameMinLength = config.property("ktor.ProductValidation.productNameMinLength").getString()?.toIntOrNull()
    val productNameMaxLength = config.property("ktor.ProductValidation.productNameMaxLength").getString()?.toIntOrNull()
}