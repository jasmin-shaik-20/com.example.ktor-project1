import com.example.dao.Login
import com.example.endpoints.ApiEndPoint
import com.example.services.LoginServices
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureLoginRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val secret = config.property("ktor.jwt.secret").getString()
    val issuer = config.property("ktor.jwt.issuer").getString()
    val audience = config.property("ktor.jwt.audience").getString()
    val loginNameMinLength = config.property("ktor.LoginValidation.loginNameMinLength").getString().toIntOrNull()
    val loginNameMaxLength = config.property("ktor.LoginValidation.loginNameMaxLength").getString().toIntOrNull()
    val loginPasswordMinLength = config.property("ktor.LoginValidation.loginPasswordMinLength")
        .getString()?.toIntOrNull()
    val loginPasswordMaxLength = config.property("ktor.LoginValidation.loginPasswordMaxLength").
    getString()?.toIntOrNull()

    val loginServices: LoginServices by inject()

    routing {
        route(ApiEndPoint.LOGIN) {
            post("/login") {
                val user = call.receive<Login>()
                val result = loginServices.handleUserLogin(
                    user = user,
                    secret = secret,
                    issuer = issuer,
                    audience = audience,
                    loginNameMinLength = loginNameMinLength,
                    loginNameMaxLength = loginNameMaxLength,
                    loginPasswordMinLength = loginPasswordMinLength,
                    loginPasswordMaxLength = loginPasswordMaxLength
                )
                call.respond(result)
            }

            authenticate("auth-jwt") {
                get("/hello") {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val result = loginServices.handleAuthenticatedHello(principal!!)
                    call.respond(result)
                }
            }
        }
    }
}
