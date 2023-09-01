package com.example.routes

import com.example.dao.Course
import com.example.endpoints.ApiEndPoint
import com.example.repository.CourseRepository
import com.example.services.CourseServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Application.configureCourseRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val courseNameMinLength = config.property("ktor.CourseValidation.courseNameMinLength").getString()?.toIntOrNull()
    val courseNameMaxLength = config.property("ktor.CourseValidation.courseNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.COURSE) {

            val courseServices = CourseServices()

            get {
                val courses = courseServices.handleGetCourses()
                call.respond(courses)
                call.application.environment.log.info("Returned a list of courses")
            }

            post {
                val courseDetails = call.receive<Course>()
                val course = courseServices.handlePostCourse(courseDetails, courseNameMinLength, courseNameMaxLength)
                call.respond(HttpStatusCode.Created, course)
                call.application.environment.log.info("Created a new course: $course")
            }

            get("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@get call.respond("Missing id")
                val course = courseServices.handleGetCourseById(id)
                call.respond(course)
                call.application.environment.log.info("Returned course with ID: $id")
            }

            delete("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@delete call.respond("Missing id")
                courseServices.handleDeleteCourse(id)
                call.respond(HttpStatusCode.OK, "Course deleted successfully")
                call.application.environment.log.info("Deleted course with ID: $id")
            }

            put("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull()?:return@put call.respond("Missing id")
                val courseDetails = call.receive<Course>()
                courseServices.handlePutCourse(id, courseDetails)
                call.respond(HttpStatusCode.OK, "Course updated successfully")
                call.application.environment.log.info("Updated course with ID: $id")
            }
        }
    }
}
