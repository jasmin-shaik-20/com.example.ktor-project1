package com.example.plugins

import com.example.dao.*
import com.example.interfaces.CourseInterfaceImpl
import com.example.interfaces.ProductInterfaceImpl
import com.example.interfaces.StudentInterfaceImpl
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    routing {

        route("/customer") {
            get {
                if (customerStorage.isNotEmpty()) {
                    call.respond(customerStorage)
                } else {
                    throw Throwable()
                }
            }

            get("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id")
                } else {
                    val customer = customerStorage.find { it.id == id }
                    if (customer == null) {
                        call.respondText("No customer with id $id")
                    } else {
                        call.respond(customer)
                    }
                }
            }

            post {
                val customer = call.receive<Customer>()
                customerStorage.add(customer)
                call.respondText("Customer stored correctly")
            }

            delete("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id")
                } else {
                    if (customerStorage.removeIf { it.id == id }) {
                        call.respondText("Customer removed correctly")
                    } else {
                        call.respondText("No customer to delete with given id $id")
                    }
                }
            }

            put("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id")
                } else {
                    val existingCustomer = customerStorage.find { it.id == id }
                    if (existingCustomer == null) {
                        call.respondText("No customer with id $id")
                    } else {
                        val updatedCustomer = call.receive<Customer>()
                        customerStorage.remove(existingCustomer)
                        customerStorage.add(updatedCustomer)
                        call.respondText("Customer updated correctly")
                    }
                }
            }
            patch("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id ")
                } else {
                    val name = call.receive<Map<String, String>>()["name"]
                    val find = customerStorage.find {
                        it.id == id
                    }
                    if (find != null) {
                        find.name = name.toString()
                        call.respond(find)
                    } else {
                        call.respondText("customer not found with the given id")
                    }
                }
            }
        }

            route("/user") {
            val usersInterfaceImpl = UsersInterfaceImpl()
            get {
                val query = transaction {
                    Users.selectAll().map {
                        mapOf("id" to it[Users.id], "username" to it[Users.name])
                    }

                }
                call.respond(query)
            }


            post {
                val userdetails = call.receive<User>()
                val user = usersInterfaceImpl.createUser(userdetails.id, userdetails.name)
                if(user!=null) {
                    call.respond(user)
                }

            }

            get("/{id?}") {
                val id=call.parameters["id"]?:return@get throw Throwable()
                val user1 = usersInterfaceImpl.selectUser(id.toInt())
                if(user1!=null){
                    call.respond(user1)
                }
                else{
                    call.respond("No user found")
                }

            }

        }

        route("/userprofile") {
            val profileInterfaceImpl = ProfileInterfaceImpl()
            get {
                val query1 = transaction {
                    Profile.selectAll().map {
                        mapOf("id" to Profile.profileid, "email" to Profile.email, "age" to Profile.age)
                    }
                }
                call.respond(query1)
            }

            post {
                val profiledetails = call.receive<UserProfile>()
                val profile = profileInterfaceImpl.createUserProfile(
                    profiledetails.profileid,
                    profiledetails.userid,
                    profiledetails.email,
                    profiledetails.age
                )
                if(profile!=null){
                    call.respond(profile)
                }

            }


            get("/{id?}"){
                val profileid=call.parameters["id"]?:return@get throw Throwable()
                val profile1=profileInterfaceImpl.getUserProfile(profileid.toInt())
                if(profile1!=null) {
                    call.respond(profile1)
                }
                else{
                    call.respond("No user profile")
                }

            }
        }

        route("/product"){
            val productInterfaceImpl=ProductInterfaceImpl()
            get{
                val getProducts= transaction {
                    Products.selectAll().
                    map { mapOf("productid" to it[Products.productid],
                        "userid" to it[Products.userid],
                        "name" to it[Products.name],
                        "price" to it[Products.price]) }
                }
                call.respond(getProducts)
            }

            post{
                val insert=call.receive<Product>()
                val postProduct=productInterfaceImpl
                    .insertProduct(insert.productid,insert.userid,insert.name,insert.price)
                if(postProduct!=null) {
                    call.respond(HttpStatusCode.Created, postProduct)
                }
            }

            get("/{id?}"){
                val id= call.parameters["id"]?:return@get call.respond("Invalid id")
                val getid=productInterfaceImpl.getProduct(id.toInt())
                if(getid!=null){
                    call.respond(getid)
                }
            }


        }

        route("/student"){
            val studentInterfaceImpl=StudentInterfaceImpl()
            get {
                val getstudents = studentInterfaceImpl.getAllStudents()
                if (getstudents != null) {
                    call.respond(getstudents)
                }
            }

            post {
                val students = call.receive<student>()
                val poststudent = studentInterfaceImpl.insertStudent(students.id, students.name)
                if (poststudent != null) {
                    call.respond(poststudent)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get call.respond("Invalid id")
                val getid=studentInterfaceImpl.getStudentById(id.toInt())
                if(getid!=null){
                    call.respond(getid)
                }
            }
        }


        route("/course"){
            val courseInterfaceImpl=CourseInterfaceImpl()
            get{
                val getcourses=courseInterfaceImpl.getAllCourses()
                if(getcourses!=null){
                    call.respond(getcourses)
                }
            }

            post{
                val courses=call.receive<course>()
                val insert=courseInterfaceImpl.insertCourse(courses.id,courses.student_id,courses.name)
                if(insert!=null){
                    call.respond(insert)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get call.respond("Invalid id")
                val getid=courseInterfaceImpl.getCourseById(id.toInt())
                if(getid!=null){
                    call.respond(getid)
                }
            }
        }

    }
}
