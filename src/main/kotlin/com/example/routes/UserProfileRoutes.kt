package com.example.routes

import com.example.config.UserProfileConfig.emailMaxLength
import com.example.config.UserProfileConfig.emailMinLength
import com.example.database.table.UserProfile
import com.example.services.UserProfileServices
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

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
