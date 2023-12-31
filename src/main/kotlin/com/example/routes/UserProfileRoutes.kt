package com.example.routes

import com.example.config.UserProfileConfig.emailMaxLength
import com.example.config.UserProfileConfig.emailMinLength
import com.example.entities.UserProfileEntity
import com.example.model.UserProfile
import com.example.model.UserProfileInput
import com.example.services.UserProfileServices
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureUserProfile() {

    routing {
        route(ApiEndPoints.USERPROFILE) {
            val userProfileServices: UserProfileServices by inject()

            get {
                val profile = userProfileServices.handleGetUserProfiles()
                call.respond(profile)
                call.application.log.info("Returned a list of user profiles")
            }

            post {
                val userProfileDetails = call.receive<UserProfileInput>()
                val profile = userProfileServices.handlePostUserProfile(
                    userProfileDetails, emailMinLength, emailMaxLength)
                call.respond(profile)
                call.application.log.info("Created a new user profile")
            }

            get("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@get call.respond("Missing id")
                val userProfile = userProfileServices.handleGetUserProfileById(id)
                call.application.log.info("User profile found by ID")
                call.respond(userProfile)
            }

            get("userId/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@get call.respond("Missing id")
                val userProfile = userProfileServices.handleGetUserProfileByUserID(id)
                call.application.log.info("User profile found by ID")
                call.respond(userProfile)
            }

            delete("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@delete call.respond("Missing id")
                userProfileServices.handleDeleteUserProfile(id)
                call.application.log.info("User profile deleted by ID")
                call.respond(HttpStatusCode.OK, "User profile deleted")
            }

            put("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@put call.respond("Missing id")
                val userProfileDetails = call.receive<UserProfileInput>()
                userProfileServices.handlePutUserProfile(id, userProfileDetails)
                call.application.log.info("User profile updated by ID")
                call.respond(HttpStatusCode.OK, "User profile updated")
            }
        }
    }
}

