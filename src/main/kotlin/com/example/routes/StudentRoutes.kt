package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.repository.StudentRepository
import com.example.services.StudentServices
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Application.configureStudentRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val studentNameMinLength = config.property("ktor.StudentValidation.studentNameMinLength").getString()?.toIntOrNull()
    val studentNameMaxLength = config.property("ktor.StudentValidation.studentNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.STUDENT) {
            val studentRepository: StudentRepository by inject()
            val studentServices: StudentServices by inject()

            get {
                studentServices.handleGetStudents(call,studentRepository)
            }

            post {
                studentServices.handlePostStudent(call, studentRepository, studentNameMinLength, studentNameMaxLength)
            }

            get("/{id?}") {
                studentServices.handleGetStudentById(call, studentRepository)
            }

            delete("/{id?}") {
                studentServices.handleDeleteStudent(call, studentRepository)
            }

            put("/{id?}") {
                studentServices.handlePutStudent(call, studentRepository)
            }
        }
    }
}



