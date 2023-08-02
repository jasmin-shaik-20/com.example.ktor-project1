package com.example.plugins
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.http.HttpStatusCode
import kotlin.Exception

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call,cause ->
            when(cause){
                is InvalidIDException -> call.respond("Missing id")
                is UserNotFoundException -> call.respond("user not found")
                is UserProfileNotFoundException -> call.respond("profile not found")
                is ProductNotFoundException -> call.respond("product not found")
                is StudentNotFoundException -> call.respond("student not found")
                is CourseNotFoundException -> call.respond("course not found")
                else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }

        }


    }


}

class InvalidIDException():Exception()
class UserNotFoundException():Exception()
class UserProfileNotFoundException():Exception()
class ProductNotFoundException():Exception()
class StudentNotFoundException():Exception()
class CourseNotFoundException():Exception()
