package com.example.plugins
import com.example.exceptions.*
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
                is UserNotFoundException -> call.respond("User not found")
                is UserInvalidNameLengthException -> call.respond("Invalid username length")
                is UserCreationFailedException -> call.respond("User creation failed")

                is UserProfileNotFoundException -> call.respond("Profile not found")
                is UserProfileInvalidEmailLengthException -> call.respond("Invalid userProfile name length")
                is UserProfileCreationFailedException -> call.respond("UserProfile creation failed")

                is ProductNotFoundException -> call.respond("Product not found")
                is ProductNameInvalidLengthException -> call.respond("Invalid product name length")
                is ProductCreationFailedException -> call.respond("Product creation failed")

                is StudentNotFoundException -> call.respond("Student not found")
                is StudentNameInvalidLengthException -> call.respond("Invalid student name length")
                is StudentCreationFailedException -> call.respond("Student creation failed")

                is CourseNotFoundException -> call.respond("Course not found")
                is CourseNameInvalidLengthException -> call.respond("Invalid course name length")
                is CourseCreationFailedException -> call.respond("Course creation failed")

                is UserSessionsInvalidLengthException -> call.respond("Invalid username and password length")

                is LoginInvalidLengthException -> call.respond("Invalid username and password length")

                is RequestValidationException -> call.respondText("${cause.message}")
            }

        }
    }
}




