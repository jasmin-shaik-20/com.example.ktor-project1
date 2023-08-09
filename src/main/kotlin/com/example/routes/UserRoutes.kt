package com.example.routes

import com.example.dao.User
import com.example.dao.Users
import com.example.interfaceimpl.UsersInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.UserNotFoundException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureUserRoutes(){
    routing{
        route("/user") {
            val usersInterfaceImpl = UsersInterfaceImpl()
            get {
                val query = transaction {
                    Users.selectAll().map {
                        mapOf("id" to it[Users.id], "username" to it[Users.name])
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
                val details = call.receive<User>()
                val user = usersInterfaceImpl.createUser(details.id, details.name)
                if(user!=null) {
                    call.respond(user)
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

        }
    }
}