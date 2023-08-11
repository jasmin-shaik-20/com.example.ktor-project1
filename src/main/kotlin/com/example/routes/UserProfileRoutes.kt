package com.example.routes

import com.example.dao.UserProfile
import com.example.file.ApiEndPoint
import com.example.interfaceimpl.ProfileInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.UserProfileNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureUserProfile(){
    routing{
        route(ApiEndPoint.USERPROFILE) {
            val profileInterfaceImpl : ProfileInterfaceImpl by inject()
            get {
                val getProfiles=profileInterfaceImpl.getAllUserProfile()
                if(getProfiles.isEmpty()) {
                    throw UserProfileNotFoundException()
                }
                else{
                    call.application.environment.log.info("All userprofile details")
                    call.respond(getProfiles)
                }
            }

            post {
                val details = call.receive<UserProfile>()
                val user = profileInterfaceImpl.getProfileByUserId(details.userId)
                if (user != null) {
                    call.respond("user exist")
                } else {
                    val profile = profileInterfaceImpl.createUserProfile(
                        details.profileId,
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
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val profile=profileInterfaceImpl.getUserProfile(id.toInt())
                if(profile!=null) {
                    call.application.environment.log.info("Profile is found")
                    call.respond(profile)
                }
                else{
                    throw UserProfileNotFoundException()
                }
            }

            delete("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val delProfile=profileInterfaceImpl.deleteUserProfile(id)
                    if(delProfile){
                        call.application.environment.log.info("userprofile is deleted")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.application.environment.log.error("No profile found with given id")
                        throw UserProfileNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val profile=call.receive<UserProfile>()
                    val editProfile=profileInterfaceImpl.editUserProfile(profile.profileId,profile.email,profile.age)
                    if(editProfile){
                        call.application.environment.log.info("Profile is updated")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw UserProfileNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}