package com.example.plugins
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.plugins.requestvalidation.RequestValidationException
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
                is RequestValidationException -> call.respondText("${cause.message}")
            }

        }
    }
}

class InvalidIDException:Exception()
class UserNotFoundException:Exception()
class UserProfileNotFoundException:Exception()
class ProductNotFoundException:Exception()
class StudentNotFoundException:Exception()
class CourseNotFoundException:Exception()

