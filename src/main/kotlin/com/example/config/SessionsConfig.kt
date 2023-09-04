package com.example.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object SessionsConfig{

    val config = HoconApplicationConfig(ConfigFactory.load())
    val sessionNameMinLength= config.property("ktor.SessionValidation.sessionNameMinLength").getString().toIntOrNull()
    val sessionNameMaxLength= config.property("ktor.SessionValidation.sessionNameMaxLength").getString().toIntOrNull()
    val sessionPasswordMinLength= config.property("ktor.SessionValidation.sessionPasswordMinLength").
    getString().toIntOrNull()
    val sessionPasswordMaxLength= config.property("ktor.SessionValidation.sessionPasswordMaxLength").
    getString().toIntOrNull()
}