package com.example.services

import com.example.dao.UserProfile
import com.example.plugins.UserProfileNotFoundException
import com.example.repository.ProfileInterfaceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class UserProfileServices {
    suspend fun handleGetUserProfiles(call:ApplicationCall,profileInterfaceImpl: ProfileInterfaceImpl) {
        val profiles = profileInterfaceImpl.getAllUserProfile()
        if (profiles.isEmpty()) {
            throw UserProfileNotFoundException()
        } else {
            call.application.environment.log.info("All userprofile details")
            call.respond(profiles)
        }
    }

    suspend fun handlePostUserProfile(
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
                    call.application.environment.log.info("Profile is created")
                    call.respond(HttpStatusCode.Created, profile)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        } else {
            call.respond("Invalid Length")
        }
    }

    suspend fun handleGetUserProfileById(
        call: ApplicationCall,
        profileInterfaceImpl: ProfileInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        val profile = profileInterfaceImpl.getUserProfile(id!!.toInt())
        if (profile != null) {
            call.application.environment.log.info("Profile is found")
            call.respond(profile)
        } else {
            throw UserProfileNotFoundException()
        }
    }

    suspend fun handleDeleteUserProfile(
        call: ApplicationCall,
        profileInterfaceImpl: ProfileInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val delProfile = profileInterfaceImpl.deleteUserProfile(id)
            if (delProfile) {
                call.application.environment.log.info("userprofile is deleted")
                call.respond(HttpStatusCode.OK, "Profile deleted")
            } else {
                call.application.environment.log.error("No profile found with given id")
                throw UserProfileNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    suspend fun handlePutUserProfile(
        call: ApplicationCall,
        profileInterfaceImpl: ProfileInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val profile = call.receive<UserProfile>()
            val editProfile = profileInterfaceImpl.editUserProfile(profile.profileId, profile.email, profile.age)
            if (editProfile) {
                call.application.environment.log.info("Profile is updated")
                call.respond(HttpStatusCode.OK)
            } else {
                throw UserProfileNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}