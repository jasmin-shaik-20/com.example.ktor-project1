package com.example.routes
import com.example.services.UserServices
import com.example.config.UserConfig.nameMaxLength
import com.example.config.UserConfig.nameMinLength
import com.example.model.User
import com.example.model.UserName
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.server.response.*
import io.ktor.server.response.respond
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
                val name = call.receive<UserName>()
                val user = userServices.handlePostUser(name, nameMinLength, nameMaxLength)
                call.respond(HttpStatusCode.Created,user)
                call.application.environment.log.info("Created a new user: $user")
            }

            get("/{id}") {
                val id = call.parameters["id"]?.let { UUID.fromString(it) }
                    ?:return@get call.respond("Missing id")
                val user = userServices.handleGetUserById(id)
                call.respondText(user.name, ContentType.Text.Plain)
                call.application.environment.log.info("Returned user with ID: $id")
            }

            delete("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@delete call.respond("Missing id")
                userServices.handleDeleteUser(id)
                call.respond(HttpStatusCode.OK, "com.example.model.User deleted successfully")
                call.application.environment.log.info("Deleted user with ID: $id")

            }

            put("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@put call.respond("Missing id")
                val userDetails = call.receive<UserName>()
                userServices.handleUpdateUser(id, userDetails)
                call.respond(HttpStatusCode.OK, "com.example.model.User updated successfully")
                call.application.environment.log.info("Updated user with ID: $id")
            }
        }
    }
}
