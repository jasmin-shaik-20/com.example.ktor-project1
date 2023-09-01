import com.example.dao.Customer
import com.example.dao.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

class CustomerServices {
    fun handleGetCustomers():List<Customer> {
        return if (customerStorage.isNotEmpty()) {
            customerStorage
        } else {
            emptyList()
        }
    }
    fun handleGetCustomerById(id: String?): Any {
        return if (id == null) {
            "Missing id"
        } else {
            customerStorage.find { it.id == id } ?: "No customer with id $id"
        }
    }

     fun handlePostCustomer(customer: Customer,
        customerNameMinLength: Int?,
        customerNameMaxLength: Int?
    ): Any {
        return if (customer.name.length in customerNameMinLength!!..customerNameMaxLength!!) {
            customerStorage.add(customer)
            "Customer stored correctly"
        } else {
            "Invalid Length"
        }
    }

    fun handleDeleteCustomer(id: String?): Any {
        if (id == null) {
            return "Missing id"
        } else {
            return if (customerStorage.removeIf { it.id == id }) {
                "Customer removed correctly"
            } else {
                "No customer to delete with given id $id"
            }
        }
    }

    fun handlePutCustomer(id: String?,updatedCustomer: Customer,
        customerNameMinLength: Int?,
        customerNameMaxLength: Int?
    ): Any {
        return if (id == null) {
            "Missing id"
        } else {
            val existingCustomer = customerStorage.find { it.id == id }
            if (existingCustomer == null) {
                "No customer with id $id"
            } else {
                if (updatedCustomer.name.length in (customerNameMinLength ?: 0)..(customerNameMaxLength ?: Int.MAX_VALUE)) {
                    customerStorage.remove(existingCustomer)
                    customerStorage.add(updatedCustomer)
                    "Customer updated correctly"
                } else {
                    "Invalid Length"
                }
            }
        }
    }

    fun handlePatchCustomer(id: String?, name: String?,
                                    customerNameMinLength: Int?, customerNameMaxLength: Int?): Any {
        return if (id == null) {
            "Missing id"
        } else {
            val existingCustomer = customerStorage.find { it.id == id }
            if (existingCustomer != null) {
                if (name != null && name.length in (customerNameMinLength ?: 0)
                    ..(customerNameMaxLength ?: Int.MAX_VALUE)) {
                    existingCustomer.name = name
                    existingCustomer
                } else {
                    "Invalid Length"
                }
            } else {
                "Customer not found with the given id"
            }
        }
    }

}
