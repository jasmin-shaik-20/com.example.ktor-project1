import com.example.dao.User
import com.example.endpoints.ApiEndPoint
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import com.example.services.UserServices
import io.ktor.server.config.*
import org.koin.ktor.ext.inject

fun Application.configureUserRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val nameMinLength = config.property("ktor.UserValidation.nameMinLength").getString()?.toIntOrNull()
    val nameMaxLength = config.property("ktor.UserValidation.nameMaxLength").getString()?.toIntOrNull()

    val userServices: UserServices by inject()

    routing {
        route(ApiEndPoint.USER) {
            get {
                val users = userServices.handleGetUsers()
                call.respond(users)
                call.application.environment.log.info("Returned a list of users")
            }

            post {
                val userDetails = call.receive<User>()
                val user = userServices.handlePostUser(userDetails, nameMinLength, nameMaxLength)
                call.respond(HttpStatusCode.Created, user)
                call.application.environment.log.info("Created a new user: $user")
            }

            get("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@get call.respond("Missing id")
                val user = userServices.handleGetUserById(id)
                call.respond(user)
                call.application.environment.log.info("Returned user with ID: $id")

            }

            delete("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@delete call.respond("Missing id")
                userServices.handleDeleteUser(id)
                call.respond(HttpStatusCode.OK, "User deleted successfully")
                call.application.environment.log.info("Deleted user with ID: $id")

            }

            put("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@put call.respond("Missing id")
                val userDetails = call.receive<User>()
                userServices.handleUpdateUser(id, userDetails)
                call.respond(HttpStatusCode.OK, "User updated successfully")
                call.application.environment.log.info("Updated user with ID: $id")
            }
        }
    }
}
