package com.example.routes

import com.example.config.CourseConfig.courseNameMaxLength
import com.example.config.CourseConfig.courseNameMinLength
import com.example.entities.CourseEntity
import com.example.model.Course
import com.example.model.CourseInput
import com.example.services.CourseServices
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureCourseRoutes() {

    routing {
        route(ApiEndPoints.COURSE) {

            val courseServices : CourseServices by inject()

            get {
                val courses = courseServices.handleGetCourses()
                call.respond(courses)
                call.application.environment.log.info("Returned a list of courses")
            }

            post {
                val courseDetails = call.receive<CourseInput>()
                val course = courseServices.handlePostCourse(courseDetails, courseNameMinLength, courseNameMaxLength)
                call.respond(HttpStatusCode.Created, course)
                call.application.environment.log.info("Created a new course: $course")
            }

            get("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@get call.respond("Missing id")
                val course = courseServices.handleGetCourseById(id)
                call.respond(course)
                call.application.environment.log.info("Returned course with ID: $id")
            }

            delete("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@delete call.respond("Missing id")
                courseServices.handleDeleteCourse(id)
                call.respond(HttpStatusCode.OK, "Course deleted successfully")
                call.application.environment.log.info("Deleted course with ID: $id")
            }

            put("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@put call.respond("Missing id")
                val courseDetails = call.receive<Course>()
                courseServices.handlePutCourse(id, courseDetails)
                call.respond(HttpStatusCode.OK, "Course updated successfully")
                call.application.environment.log.info("Updated course with ID: $id")
            }
        }
    }
}
