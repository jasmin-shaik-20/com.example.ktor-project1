package com.example.routes

import com.example.dao.User
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.UsersInterfaceImpl
import com.example.plugins.UserNotFoundException
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.util.pipeline.*

import org.koin.ktor.ext.inject

fun Application.configureUserRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val nameMinLength = config.property("ktor.UserValidation.nameMinLength").getString()?.toIntOrNull()
    val nameMaxLength = config.property("ktor.UserValidation.nameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.USER) {
            val usersInterfaceImpl: UsersInterfaceImpl by inject()

            get {
                handleGetUsers(usersInterfaceImpl)
            }

            post {
                handlePostUser(call, usersInterfaceImpl, nameMinLength, nameMaxLength)
            }

            get("/{id?}") {
                handleGetUserById(call, usersInterfaceImpl)
            }

            delete("/{id?}") {
                handleDeleteUser(call, usersInterfaceImpl)
            }

            put("/{id?}") {
                handlePutUser(call, usersInterfaceImpl)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetUsers(usersInterfaceImpl: UsersInterfaceImpl) {
    val getUsers = usersInterfaceImpl.getAllUsers()
    if (getUsers.isEmpty()) {
        call.application.environment.log.error("No users found")
        call.respond("No users found")
    } else {
        call.respond(getUsers)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostUser(
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

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetUserById(
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

private suspend fun PipelineContext<Unit, ApplicationCall>.handleDeleteUser(
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

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePutUser(
    call: ApplicationCall,
    usersInterfaceImpl: UsersInterfaceImpl
) {
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
