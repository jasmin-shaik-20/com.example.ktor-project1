package com.example

import io.ktor.server.application.Application
import com.example.plugins.configureLogin
import com.example.plugins.configureSerialization
import com.example.plugins.configureKoin
import com.example.plugins.configureRouting
import com.example.plugins.configureValidation
import com.example.plugins.configureDatabase
import com.example.plugins.configureSessions
import com.example.plugins.configureStatusPages
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureLogin()
    configureSerialization()
    configureKoin()
    configureRouting()
    configureValidation()
    configureDatabase()
    configureSessions()
    configureStatusPages()
}
