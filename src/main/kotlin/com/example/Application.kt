package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    embeddedServer(Netty, port = getPortFromEnv(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

private fun getPortFromEnv(): Int {
    return System.getenv("PORT")?.toIntOrNull() ?: 8080
}


fun Application.module() {
    configureLogin()
    configureSerialization()
    configureKoin()
    configureRouting()
    configureRequestValidation()
    configureDatabase()
    configureSessions()
    configureStatusPages()
}
