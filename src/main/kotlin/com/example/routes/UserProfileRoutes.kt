package com.example.routes

import com.example.dao.UserProfile
import com.example.endpoints.ApiEndPoint
import com.example.repository.ProfileInterfaceImpl
import com.example.plugins.UserProfileNotFoundException
import com.example.services.UserProfileServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.ApplicationCall
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.util.pipeline.PipelineContext
import org.koin.ktor.ext.inject


fun Application.configureUserProfile() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val emailMinLength = config.property("ktor.ProfileValidation.emailMinLength").getString()?.toIntOrNull()
    val emailMaxLength = config.property("ktor.ProfileValidation.emailMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.USERPROFILE) {
            val profileInterfaceImpl: ProfileInterfaceImpl by inject()
            val userProfileServices: UserProfileServices by inject()

            get {
                userProfileServices.handleGetUserProfiles(call,profileInterfaceImpl)
            }

            post {
                userProfileServices.handlePostUserProfile(call, profileInterfaceImpl, emailMinLength, emailMaxLength)
            }

            get("/{id?}") {
                userProfileServices.handleGetUserProfileById(call, profileInterfaceImpl)
            }

            delete("/{id?}") {
                userProfileServices.handleDeleteUserProfile(call, profileInterfaceImpl)
            }

            put("/{id?}") {
                userProfileServices.handlePutUserProfile(call, profileInterfaceImpl)
            }
        }
    }
}



