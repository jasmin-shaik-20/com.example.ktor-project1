package com.example.routes
import UserServices
import com.example.config.UserConfig.nameMaxLength
import com.example.config.UserConfig.nameMinLength
import com.example.entities.UserEntity
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import com.example.utils.appConstants.ApiEndPoints
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureUserRoutes() {

    val userServices: UserServices by inject()

    routing {
        route(ApiEndPoints.USER) {
            get {
                val users = userServices.handleGetUsers()
                call.respond(users)
                call.application.environment.log.info("Returned a list of users")
            }

            post {
                val userDetails = call.receive<UserEntity>()
                val user = userServices.handlePostUser(userDetails, nameMinLength, nameMaxLength)
                call.respond(HttpStatusCode.Created, user)
                call.application.environment.log.info("Created a new user: $user")
            }

            get("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@get call.respond("Missing id")
                val user = userServices.handleGetUserById(id)
                call.respond(user)
                call.application.environment.log.info("Returned user with ID: $id")
            }

            delete("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@delete call.respond("Missing id")
                userServices.handleDeleteUser(id)
                call.respond(HttpStatusCode.OK, "User deleted successfully")
                call.application.environment.log.info("Deleted user with ID: $id")

            }

            put("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@put call.respond("Missing id")
                val userDetails = call.receive<UserEntity>()
                userServices.handleUpdateUser(id, userDetails)
                call.respond(HttpStatusCode.OK, "User updated successfully")
                call.application.environment.log.info("Updated user with ID: $id")
            }
        }
    }
}
