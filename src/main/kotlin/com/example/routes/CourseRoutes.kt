package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.repository.CourseRepository
import com.example.services.CourseServices
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

fun Application.configureCourseRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val courseNameMinLength = config.property("ktor.CourseValidation.courseNameMinLength").getString()?.toIntOrNull()
    val courseNameMaxLength = config.property("ktor.CourseValidation.courseNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.COURSE) {
            val courseRepository: CourseRepository by inject()
            val courseServices=CourseServices()

            get {
                courseServices.handleGetCourses(call,courseRepository)
            }

            post {
                courseServices.handlePostCourse(call, courseRepository,courseNameMinLength,courseNameMaxLength)
            }

            get("/{id?}") {
                courseServices.handleGetCourseById(call, courseRepository)
            }

            delete("/{id?}") {
                courseServices.handleDeleteCourse(call, courseRepository)
            }

            put("/{id?}") {
                courseServices.handlePutCourse(call, courseRepository)
            }
        }
    }
}

