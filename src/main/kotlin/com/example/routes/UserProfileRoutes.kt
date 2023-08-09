package com.example.routes

import com.example.dao.UserProfile
import com.example.interfaceimpl.ProfileInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.UserProfileNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureUserProfile(){
    routing{
        route("/userprofile") {
            val profileInterfaceImpl = ProfileInterfaceImpl()
            get {
                val getProfiles=profileInterfaceImpl.getAllUSerProfile()
                call.respond(getProfiles)
            }

            post {
                val details = call.receive<UserProfile>()
                val profile = profileInterfaceImpl.createUserProfile(
                    details.profileId,
                    details.userId,
                    details.email,
                    details.age
                )
                if(profile!=null){
                    call.respond(HttpStatusCode.Created,profile)
                }
                else{
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val profile=profileInterfaceImpl.getUserProfile(id.toInt())
                if(profile!=null) {
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
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.respond(HttpStatusCode.NotFound)
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
                    val editUser=profileInterfaceImpl.editUserProfile(profile.profileId,profile.email,profile.age)
                    if(editUser){
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}