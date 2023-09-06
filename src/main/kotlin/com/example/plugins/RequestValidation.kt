package com.example.plugins

import com.example.database.table.*
import com.example.entities.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<UserEntity> { bodyText -> validateUser(bodyText) }
        validate<UserProfileEntity> { bodyText -> validateUserProfile(bodyText) }
        validate<ProductEntity> { bodyText -> validateProduct(bodyText) }
        validate<StudentEntity> { bodyText -> validateStudent(bodyText) }
        validate<CourseEntity> { bodyText -> validateCourse(bodyText) }
    }
}

private fun validateUser(bodyText: UserEntity): ValidationResult {
    return when {
        bodyText.name.isBlank() -> ValidationResult.Invalid("Name should not be empty")
        !bodyText.name.matches(Regex("[a-zA-Z]+")) -> ValidationResult.Invalid("Name should contain alphabetic")
        else -> ValidationResult.Valid
    }
}

private fun validateUserProfile(bodyText: UserProfileEntity): ValidationResult {
    return when {
        bodyText.email.isBlank() -> ValidationResult.Invalid("Email should not be empty")
        !bodyText.email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) ->
            ValidationResult.Invalid("Invalid email address")
        bodyText.age <= 0 -> ValidationResult.Invalid("Age must be positive")
        else -> ValidationResult.Valid
    }
}

private fun validateProduct(bodyText: ProductEntity): ValidationResult {
    return when {
        bodyText.name.isBlank() -> ValidationResult.Invalid("Product name should not be empty")
        bodyText.price <= 0 -> ValidationResult.Invalid("Price should be greater than zero")
        else -> ValidationResult.Valid
    }
}

private fun validateStudent(bodyText: StudentEntity): ValidationResult {
    return when {
        bodyText.name.isBlank() -> ValidationResult.Invalid("Name should not be empty")
        !bodyText.name.matches(Regex("[a-zA-Z]+")) -> ValidationResult.Invalid("Name should contain alphabetic")
        else -> ValidationResult.Valid
    }
}

private fun validateCourse(bodyText: CourseEntity): ValidationResult {
    return when {
        bodyText.name.isBlank() -> ValidationResult.Invalid("CourseName should not be empty")
        else -> ValidationResult.Valid
    }
}

