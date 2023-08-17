package com.example.routes

import com.example.dao.User
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.UsersInterfaceImpl
import com.example.plugins.UserNotFoundException
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import org.koin.ktor.ext.inject

fun Application.configureUserRoutes(){
    val config = HoconApplicationConfig(ConfigFactory.load())
    val nameMinLength= config.property("ktor.UserValidation.nameMinLength").getString()?.toIntOrNull()
    val nameMaxLength= config.property("ktor.UserValidation.nameMaxLength").getString()?.toIntOrNull()
    routing{
        route(ApiEndPoint.USER) {
            val usersInterfaceImpl : UsersInterfaceImpl by inject()

            get{
               val getUsers=usersInterfaceImpl.getAllUsers()
                if(getUsers.isEmpty()) {
                    call.application.environment.log.error("No users found")
                    call.respond("No users found")
                }
                else{
                    call.respond(getUsers)
                }
            }

            post {
                val details = call.receive<User>()
                if(details.name.length in nameMinLength!!..nameMaxLength!!) {
                    val user = usersInterfaceImpl.createUser(details.id, details.name)
                    if (user != null) {
                        call.application.environment.log.info("User created $user")
                        call.respond(HttpStatusCode.Created, user)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
                else{
                    call.respond("Invalid length")
                }
            }

            get("/{id?}") {
                val id=call.parameters["id"]?.toIntOrNull()
                val user = usersInterfaceImpl.selectUser(id!!.toInt())
                if(user!=null){
                    call.application.environment.log.info("user found with given id")
                    call.respond(user)
                }
                else{
                    throw UserNotFoundException()
                }
            }

            delete("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val delUser=usersInterfaceImpl.deleteUser(id)
                    if(delUser){
                        call.application.environment.log.info("User is deleted")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.application.environment.log.error("No user found with given id")
                        throw UserNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val user=call.receive<User>()
                    val editUser=usersInterfaceImpl.editUser(user.id,user.name)
                    if(editUser){
                        call.application.environment.log.info("User is updated")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw UserNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}