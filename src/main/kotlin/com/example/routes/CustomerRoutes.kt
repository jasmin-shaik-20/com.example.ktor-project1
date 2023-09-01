import com.example.dao.Customer
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCustomerRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val customerNameMinLength = config.property("ktor.CustomerValidation.customerNameMinLength").getString()?.toIntOrNull()
    val customerNameMaxLength = config.property("ktor.CustomerValidation.customerNameMaxLength").getString()?.toIntOrNull()

    val customerServices = CustomerServices()

    routing {
        route("/customers") {

            get {
                val customers = customerServices.handleGetCustomers()
                call.respond(customers)
            }

            get("/{id?}") {
                val id = call.parameters["id"]
                val customer = customerServices.handleGetCustomerById(id)
                call.respond(customer)
            }

            post {
                val customer = call.receive<Customer>()
                val result = customerServices.handlePostCustomer(customer,customerNameMinLength, customerNameMaxLength)
                call.respond(result)
            }

            delete("/{id?}") {
                val id = call.parameters["id"]
                val result = customerServices.handleDeleteCustomer(id)
                call.respond(result)
            }

            put("/{id?}") {
                val id = call.parameters["id"]
                val updatedCustomer = call.receive<Customer>()
                val result = customerServices.handlePutCustomer(id,updatedCustomer,customerNameMinLength, customerNameMaxLength)
                call.respond(result)
            }

            patch("/{id?}") {
                val id = call.parameters["id"]
                val name = call.receive<Map<String, String>>().get("name")
                call.respond(customerServices.handlePatchCustomer(id, name, customerNameMinLength, customerNameMaxLength))
            }
        }
    }
}
