package com.example.plugins

import com.example.di.appModule
import com.example.repository.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(){

    install(Koin){
        modules(appModule)
    }
}
