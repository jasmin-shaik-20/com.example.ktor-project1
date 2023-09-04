package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object CourseConfig {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val courseNameMinLength = config.property("ktor.CourseValidation.courseNameMinLength").getString()?.toIntOrNull()
    val courseNameMaxLength = config.property("ktor.CourseValidation.courseNameMaxLength").getString()?.toIntOrNull()

}