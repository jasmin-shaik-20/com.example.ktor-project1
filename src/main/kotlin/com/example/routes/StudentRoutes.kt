package com.example.routes

import com.example.dao.Student
import com.example.endpoints.ApiEndPoint
import com.example.repository.StudentInterfaceImpl
import com.example.plugins.StudentNotFoundException
import com.example.services.StudentServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.ApplicationCall
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.util.pipeline.PipelineContext
import org.koin.ktor.ext.inject

fun Application.configureStudentRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val studentNameMinLength = config.property("ktor.StudentValidation.studentNameMinLength").getString()?.toIntOrNull()
    val studentNameMaxLength = config.property("ktor.StudentValidation.studentNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.STUDENT) {
            val studentInterfaceImpl: StudentInterfaceImpl by inject()
            val studentServices: StudentServices by inject()

            get {
                studentServices.handleGetStudents(call,studentInterfaceImpl)
            }

            post {
                studentServices.handlePostStudent(call, studentInterfaceImpl, studentNameMinLength, studentNameMaxLength)
            }

            get("/{id?}") {
                studentServices.handleGetStudentById(call, studentInterfaceImpl)
            }

            delete("/{id?}") {
                studentServices.handleDeleteStudent(call, studentInterfaceImpl)
            }

            put("/{id?}") {
                studentServices.handlePutStudent(call, studentInterfaceImpl)
            }
        }
    }
}



