package com.example.routes

import com.example.dao.UserProfile
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.ProfileInterfaceImpl
import com.example.plugins.UserProfileNotFoundException
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

            get {
                handleGetUserProfiles(profileInterfaceImpl)
            }

            post {
                handlePostUserProfile(call, profileInterfaceImpl, emailMinLength, emailMaxLength)
            }

            get("/{id?}") {
                handleGetUserProfileById(call, profileInterfaceImpl)
            }

            delete("/{id?}") {
                handleDeleteUserProfile(call, profileInterfaceImpl)
            }

            put("/{id?}") {
                handlePutUserProfile(call, profileInterfaceImpl)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.
        handleGetUserProfiles(profileInterfaceImpl: ProfileInterfaceImpl) {
    val profiles = profileInterfaceImpl.getAllUserProfile()
    if (profiles.isEmpty()) {
        throw UserProfileNotFoundException()
    } else {
        application.environment.log.info("All userprofile details")
        call.respond(profiles)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostUserProfile(
    call: ApplicationCall,
    profileInterfaceImpl: ProfileInterfaceImpl,
    emailMinLength: Int?,
    emailMaxLength: Int?
) {
    val details = call.receive<UserProfile>()
    if (details.email.length in emailMinLength!!..emailMaxLength!!) {
        val user = profileInterfaceImpl.getProfileByUserId(details.userId)
        if (user != null) {
            call.respond("user exist")
        } else {
            val profile = profileInterfaceImpl.createUserProfile(
                details.userId,
                details.email,
                details.age
            )
            if (profile != null) {
                application.environment.log.info("Profile is created")
                call.respond(HttpStatusCode.Created, profile)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    } else {
        call.respond("Invalid Length")
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetUserProfileById(
    call: ApplicationCall,
    profileInterfaceImpl: ProfileInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    val profile = profileInterfaceImpl.getUserProfile(id!!.toInt())
    if (profile != null) {
        application.environment.log.info("Profile is found")
        call.respond(profile)
    } else {
        throw UserProfileNotFoundException()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleDeleteUserProfile(
    call: ApplicationCall,
    profileInterfaceImpl: ProfileInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val delProfile = profileInterfaceImpl.deleteUserProfile(id)
        if (delProfile) {
            application.environment.log.info("userprofile is deleted")
            call.respond(HttpStatusCode.OK, "Profile deleted")
        } else {
            application.environment.log.error("No profile found with given id")
            throw UserProfileNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePutUserProfile(
    call: ApplicationCall,
    profileInterfaceImpl: ProfileInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val profile = call.receive<UserProfile>()
        val editProfile = profileInterfaceImpl.editUserProfile(profile.profileId, profile.email, profile.age)
        if (editProfile) {
            application.environment.log.info("Profile is updated")
            call.respond(HttpStatusCode.OK)
        } else {
            throw UserProfileNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}

