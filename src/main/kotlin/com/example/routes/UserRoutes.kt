package com.example.routes

import com.example.dao.User
import com.example.dao.Users
import com.example.plugins.InvalidIDException
import com.example.plugins.UserNotFoundException
import com.example.plugins.UsersInterfaceImpl
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.text.get

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
                val userdetails = call.receive<User>()
                val user = usersInterfaceImpl.createUser(userdetails.id, userdetails.name)
                if(user!=null) {
                    call.respond(user)
                }

            }

            get("/{id?}") {
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val user1 = usersInterfaceImpl.selectUser(id.toInt())
                if(user1!=null){
                    call.respond(user1)
                }
                else{
                    throw UserNotFoundException()
                }

            }

        }
    }
}