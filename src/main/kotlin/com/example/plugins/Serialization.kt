package com.example.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.application.Application
import io.ktor.server.application.install
import kotlinx.serialization.json.Json.Default.serializersModule
import org.koin.dsl.module
import java.util.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}



