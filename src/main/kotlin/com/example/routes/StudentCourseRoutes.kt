package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.repository.StudentCourseRepository
import com.example.services.StudentCourseServices
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Application.configureStudentCourseRoutes(){
    routing{
        route(ApiEndPoint.STUDENTCOURSES){

            val studentCourseServices:StudentCourseServices by inject()

            get("/courses/{studentId}") {
                val studentId = call.parameters["studentId"]?.toIntOrNull()
                    ?: return@get call.respond("Invalid studentId")
                val courses = studentCourseServices.handleGetCoursesByStudentId(studentId)
                call.respond(courses)
            }

            get("/students/{courseId}") {
                val courseId = call.parameters["courseId"]?.toIntOrNull()
                    ?: return@get call.respond("Invalid courseId")
                val students = studentCourseServices.handleGetStudentsByCourseId(courseId)
                call.respond(students)
            }
        }
    }
}
