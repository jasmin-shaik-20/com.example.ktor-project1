package com.example.routes

import com.example.dao.User
import com.example.dao.Users
import com.example.file.ApiEndPoint
import com.example.interfaceimpl.UsersInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.UserNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureUserRoutes(){
    routing{
        route(ApiEndPoint.USER) {
            val usersInterfaceImpl = UsersInterfaceImpl()
            get("/") {
               val getUsers=usersInterfaceImpl.getAllUsers()
                if(getUsers.isEmpty()) {
                    throw UserNotFoundException()
                }
                else{
                    call.respond(getUsers)
                }
            }

            post {
                val details = call.receive<User>()
                val user = usersInterfaceImpl.createUser(details.id, details.name)
                if(user!=null) {
                    call.respond(HttpStatusCode.Created,user)
                }
                else{
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            get("/{id?}") {
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val user = usersInterfaceImpl.selectUser(id.toInt())
                if(user!=null){
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

            put("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val user=call.receive<User>()
                    val editUser=usersInterfaceImpl.editUser(user.id,user.name)
                    if(editUser){
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