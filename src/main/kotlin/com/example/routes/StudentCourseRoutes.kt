package com.example.routes

import com.example.services.StudentCourseServices
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureStudentCourseRoutes(){

    routing{

        route(ApiEndPoints.STUDENTCOURSES){

            val studentCourseServices:StudentCourseServices by inject()

            get("/courses/{studentId}") {
                val studentId = runCatching { UUID.fromString(call.parameters["studentId"] ?: "") }
                    .getOrNull()?:return@get call.respond("Missing studentId")
                val courses = studentCourseServices.handleGetCoursesByStudentId(studentId)
                call.respond(courses)
            }

            get("/students/{courseId}") {
                val courseId = runCatching { UUID.fromString(call.parameters["courseId"] ?: "") }
                    .getOrNull()?: return@get call.respond("Missing courseId")
                val students = studentCourseServices.handleGetStudentsByCourseId(courseId)
                call.respond(students)
            }
        }
    }
}
