package com.example.routes

import com.example.dao.Profile
import com.example.dao.UserProfile
import com.example.interfaceimpl.ProfileInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.UserProfileNotFoundException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureUserProfile(){
    routing{
        route("/userprofile") {
            val profileInterfaceImpl = ProfileInterfaceImpl()
            get {
                val query = transaction {
                    Profile.selectAll().map {
                        mapOf("id" to Profile.profileId, "email" to Profile.email, "age" to Profile.age)
                    }
                }
                if(query!=null) {
                    call.respond(query)
                }
                else{
                    throw Throwable()
                }
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
                    call.respond(profile)
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
        }
    }
}