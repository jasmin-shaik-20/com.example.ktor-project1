package com.example.routes

import com.example.dao.Profile
import com.example.dao.UserProfile
import com.example.plugins.InvalidIDException
import com.example.plugins.ProfileInterfaceImpl
import com.example.plugins.UserProfileNotFoundException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.text.get

fun Application.configureUserProfile(){
    routing{
        route("/userprofile") {
            val profileInterfaceImpl = ProfileInterfaceImpl()
            get {
                val query1 = transaction {
                    Profile.selectAll().map {
                        mapOf("id" to Profile.profileid, "email" to Profile.email, "age" to Profile.age)
                    }
                }
                if(query1!=null) {
                    call.respond(query1)
                }
                else{
                    throw Throwable()
                }
            }

            post {
                val profiledetails = call.receive<UserProfile>()
                val profile = profileInterfaceImpl.createUserProfile(
                    profiledetails.profileid,
                    profiledetails.userid,
                    profiledetails.email,
                    profiledetails.age
                )
                if(profile!=null){
                    call.respond(profile)
                }

            }


            get("/{id?}"){
                val profileid=call.parameters["id"]?:return@get throw InvalidIDException()
                val profile1=profileInterfaceImpl.getUserProfile(profileid.toInt())
                if(profile1!=null) {
                    call.respond(profile1)
                }
                else{
                    throw UserProfileNotFoundException()
                }

            }
        }

    }
}