package com.example.services

import com.example.repository.UsersInterfaceImpl
import com.example.dao.User
import com.example.plugins.UserNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class UserServices {

    suspend fun handleGetUsers(call: ApplicationCall, usersInterfaceImpl: UsersInterfaceImpl) {
        val getUsers = usersInterfaceImpl.getAllUsers()
        if (getUsers.isEmpty()) {
            call.application.environment.log.error("No users found")
            call.respond("No users found")
        } else {
            call.respond(getUsers)
        }
    }

    suspend fun handlePostUser(
        call: ApplicationCall,
        usersInterfaceImpl: UsersInterfaceImpl,
        nameMinLength: Int?,
        nameMaxLength: Int?
    ) {
        val details = call.receive<User>()
        if (details.name.length in nameMinLength!!..nameMaxLength!!) {
            val user = usersInterfaceImpl.createUser(details.id, details.name)
            if (user != null) {
                call.application.environment.log.info("User created $user")
                call.respond(HttpStatusCode.Created, user)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond("Invalid length")
        }
    }

    suspend fun handleGetUserById(
        call: ApplicationCall,
        usersInterfaceImpl: UsersInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        val user = usersInterfaceImpl.selectUser(id!!.toInt())
        if (user != null) {
            call.application.environment.log.info("user found with given id")
            call.respond(user)
        } else {
            throw UserNotFoundException()
        }
    }

    suspend fun handleDeleteUser(
        call: ApplicationCall,
        usersInterfaceImpl: UsersInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val delUser = usersInterfaceImpl.deleteUser(id)
            if (delUser) {
                call.application.environment.log.info("User is deleted")
                call.respond(HttpStatusCode.OK)
            } else {
                call.application.environment.log.error("No user found with given id")
                throw UserNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    suspend fun handlePutUser(call: ApplicationCall, usersInterfaceImpl: UsersInterfaceImpl) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val user = call.receive<User>()
            val editUser = usersInterfaceImpl.editUser(user.id, user.name)
            if (editUser) {
                call.application.environment.log.info("User is updated")
                call.respond(HttpStatusCode.OK)
            } else {
                throw UserNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}