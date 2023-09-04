import com.example.config.LoginConfig.audience
import com.example.config.LoginConfig.issuer
import com.example.config.LoginConfig.loginNameMaxLength
import com.example.config.LoginConfig.loginNameMinLength
import com.example.config.LoginConfig.loginPasswordMaxLength
import com.example.config.LoginConfig.loginPasswordMinLength
import com.example.config.LoginConfig.secret
import com.example.database.table.Login
import com.example.services.LoginServices
import com.example.utils.appConstants.ApiEndPoints
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

    val loginServices: LoginServices by inject()

    routing {
        route(ApiEndPoints.LOGIN) {
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
