package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object StudentConfig {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val studentNameMinLength = config.property("ktor.StudentValidation.studentNameMinLength").getString()?.toIntOrNull()
    val studentNameMaxLength = config.property("ktor.StudentValidation.studentNameMaxLength").getString()?.toIntOrNull()
}