package com.example.routes

import com.example.dao.Course
import com.example.endpoints.ApiEndPoint
import com.example.repository.CourseInterfaceImpl
import com.example.plugins.CourseNotFoundException
import com.example.services.CourseServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.application
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

fun Application.configureCourseRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val courseNameMinLength = config.property("ktor.CourseValidation.courseNameMinLength").getString()?.toIntOrNull()
    val courseNameMaxLength = config.property("ktor.CourseValidation.courseNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.COURSE) {
            val courseInterfaceImpl: CourseInterfaceImpl by inject()
            val courseServices: CourseServices by inject()

            get {
                courseServices.handleGetCourses(call,courseInterfaceImpl)
            }

            post {
                courseServices.handlePostCourse(call, courseInterfaceImpl,courseNameMinLength,courseNameMaxLength)
            }

            get("/{id?}") {
                courseServices.handleGetCourseById(call, courseInterfaceImpl)
            }

            delete("/{id?}") {
                courseServices.handleDeleteCourse(call, courseInterfaceImpl)
            }

            put("/{id?}") {
                courseServices.handlePutCourse(call, courseInterfaceImpl)
            }
        }
    }
}

