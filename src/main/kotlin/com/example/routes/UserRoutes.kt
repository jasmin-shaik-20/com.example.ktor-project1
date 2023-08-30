package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.repository.UsersInterfaceImpl
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import com.example.services.*

import org.koin.ktor.ext.inject

fun Application.configureUserRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val nameMinLength = config.property("ktor.UserValidation.nameMinLength").getString()?.toIntOrNull()
    val nameMaxLength = config.property("ktor.UserValidation.nameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.USER) {
            val usersInterfaceImpl: UsersInterfaceImpl by inject()
            val userServices: UserServices by inject()

            get {
                userServices.handleGetUsers(call, usersInterfaceImpl)
            }

            post {
                userServices.handlePostUser(call, usersInterfaceImpl, nameMinLength, nameMaxLength)
            }

            get("/{id?}") {
                userServices.handleGetUserById(call, usersInterfaceImpl)
            }

            delete("/{id?}") {
                userServices.handleDeleteUser(call, usersInterfaceImpl)
            }

            put("/{id?}") {
                userServices.handlePutUser(call, usersInterfaceImpl)
            }
        }
    }
}



