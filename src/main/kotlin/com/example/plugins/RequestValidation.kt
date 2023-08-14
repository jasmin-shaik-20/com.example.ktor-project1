package com.example.plugins

import com.example.dao.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation(){
    install(RequestValidation) {
        validate<User> { bodyText ->
            when {
                bodyText.name.isBlank() -> ValidationResult.Invalid("Name should not be empty")
                !bodyText.name.matches(Regex("[a-zA-Z]+")) -> ValidationResult.Invalid("Name should contain alphabetic")
                else -> ValidationResult.Valid
            }
        }

        validate<UserProfile> { bodyText ->
            when {
                bodyText.userId <= 0 -> ValidationResult.Invalid("userId should be greater than 0")
                bodyText.email.isBlank() -> ValidationResult.Invalid("Email should not be empty")
                !bodyText.email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) -> ValidationResult.Invalid("Invalid email address")
                bodyText.age <= 0 -> ValidationResult.Invalid("Age must be positive")
                else -> ValidationResult.Valid
            }
        }

        validate<Product> { bodyText ->
            when {
                bodyText.userId <= 0 -> ValidationResult.Invalid("userId should be greater than zero")
                bodyText.name.isBlank() -> ValidationResult.Invalid("Product name should not be empty")
                bodyText.price <= 0 -> ValidationResult.Invalid("Price should be greater than zero")
                else -> ValidationResult.Valid
            }
        }

        validate<Student> { bodyText ->
            when {
                bodyText.name.isBlank() -> ValidationResult.Invalid("Name should not be empty")
                !bodyText.name.matches(Regex("[a-zA-Z]+")) -> ValidationResult.Invalid("Name should contain alphabetic")
                else -> ValidationResult.Valid
            }
        }

        validate<Course> { bodyText ->
            when {
                bodyText.studentId <= 0 -> ValidationResult.Invalid("StudentId should be greater than zero")
                bodyText.name.isBlank() -> ValidationResult.Invalid("CourseName should not be empty")
                else -> ValidationResult.Valid
            }
        }


    }
}