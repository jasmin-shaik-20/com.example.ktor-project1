package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.repository.ProfileRepository
import com.example.services.UserProfileServices
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
import org.koin.ktor.ext.inject


fun Application.configureUserProfile() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val emailMinLength = config.property("ktor.ProfileValidation.emailMinLength").getString()?.toIntOrNull()
    val emailMaxLength = config.property("ktor.ProfileValidation.emailMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.USERPROFILE) {
            val profileRepository: ProfileRepository by inject()
            val userProfileServices=UserProfileServices()

            get {
                userProfileServices.handleGetUserProfiles(call,profileRepository)
            }

            post {
                userProfileServices.handlePostUserProfile(call, profileRepository, emailMinLength, emailMaxLength)
            }

            get("/{id?}") {
                userProfileServices.handleGetUserProfileById(call, profileRepository)
            }

            delete("/{id?}") {
                userProfileServices.handleDeleteUserProfile(call, profileRepository)
            }

            put("/{id?}") {
                userProfileServices.handlePutUserProfile(call, profileRepository)
            }
        }
    }
}



