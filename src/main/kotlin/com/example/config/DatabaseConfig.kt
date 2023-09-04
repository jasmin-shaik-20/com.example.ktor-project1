package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object DatabaseConfig {

    val config = HoconApplicationConfig(ConfigFactory.load())
    val driver = config.property("ktor.database.driver").getString()
    val url = config.property("ktor.database.url").getString()
    val user = config.property("ktor.database.user").getString()
    val password = config.property("ktor.database.password").getString()

}