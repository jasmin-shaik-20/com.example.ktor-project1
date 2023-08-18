package com.example.plugins

import com.example.dao.User
import com.example.dao.UserProfile
import com.example.dao.Product
import com.example.dao.Student
import com.example.dao.Course
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<User> { bodyText -> validateUser(bodyText) }
        validate<UserProfile> { bodyText -> validateUserProfile(bodyText) }
        validate<Product> { bodyText -> validateProduct(bodyText) }
        validate<Student> { bodyText -> validateStudent(bodyText) }
        validate<Course> { bodyText -> validateCourse(bodyText) }
    }
}

private fun validateUser(bodyText: User): ValidationResult {
    return when {
        bodyText.name.isBlank() -> ValidationResult.Invalid("Name should not be empty")
        !bodyText.name.matches(Regex("[a-zA-Z]+")) -> ValidationResult.Invalid("Name should contain alphabetic")
        else -> ValidationResult.Valid
    }
}

private fun validateUserProfile(bodyText: UserProfile): ValidationResult {
    return when {
        bodyText.userId <= 0 -> ValidationResult.Invalid("userId should be greater than 0")
        bodyText.email.isBlank() -> ValidationResult.Invalid("Email should not be empty")
        !bodyText.email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) ->
            ValidationResult.Invalid("Invalid email address")
        bodyText.age <= 0 -> ValidationResult.Invalid("Age must be positive")
        else -> ValidationResult.Valid
    }
}

private fun validateProduct(bodyText: Product): ValidationResult {
    return when {
        bodyText.userId <= 0 -> ValidationResult.Invalid("userId should be greater than zero")
        bodyText.name.isBlank() -> ValidationResult.Invalid("Product name should not be empty")
        bodyText.price <= 0 -> ValidationResult.Invalid("Price should be greater than zero")
        else -> ValidationResult.Valid
    }
}

private fun validateStudent(bodyText: Student): ValidationResult {
    return when {
        bodyText.name.isBlank() -> ValidationResult.Invalid("Name should not be empty")
        !bodyText.name.matches(Regex("[a-zA-Z]+")) -> ValidationResult.Invalid("Name should contain alphabetic")
        else -> ValidationResult.Valid
    }
}

private fun validateCourse(bodyText: Course): ValidationResult {
    return when {
        bodyText.studentId <= 0 -> ValidationResult.Invalid("StudentId should be greater than zero")
        bodyText.name.isBlank() -> ValidationResult.Invalid("CourseName should not be empty")
        else -> ValidationResult.Valid
    }
}

