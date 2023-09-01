package com.example.routes

import com.example.dao.UserProfile
import com.example.endpoints.ApiEndPoint
import com.example.plugins.UserProfileAlreadyExistFoundException
import com.example.plugins.UserProfileNotFoundException
import com.example.repository.ProfileRepository
import com.example.services.UserProfileServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureUserProfile() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val emailMinLength = config.property("ktor.ProfileValidation.emailMinLength").getString()?.toIntOrNull()
    val emailMaxLength = config.property("ktor.ProfileValidation.emailMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.USERPROFILE) {
            val userProfileServices: UserProfileServices by inject()

            get {
                val profile = userProfileServices.handleGetUserProfiles()
                call.respond(profile)
                call.application.log.info("Returned a list of user profiles")
            }

            post {
                val userProfileDetails = call.receive<UserProfile>()
                val profile = userProfileServices.handlePostUserProfile(
                    userProfileDetails, emailMinLength, emailMaxLength)
                call.respond(profile)
                call.application.log.info("Created a new user profile")
            }

            get("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@get call.respond("Missing id")
                val userProfile = userProfileServices.handleGetUserProfileById(id)
                call.application.log.info("User profile found by ID")
                call.respond(userProfile)
            }

            delete("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@delete call.respond("Missing id")
                userProfileServices.handleDeleteUserProfile(id)
                call.application.log.info("User profile deleted by ID")
                call.respond(HttpStatusCode.OK, "User profile deleted")
            }

            put("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@put call.respond("Missing id")
                val userProfileDetails = call.receive<UserProfile>()
                userProfileServices.handlePutUserProfile(id, userProfileDetails)
                call.application.log.info("User profile updated by ID")
                call.respond(HttpStatusCode.OK, "User profile updated")
            }
        }
    }
}
