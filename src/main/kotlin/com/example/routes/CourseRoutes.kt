package com.example.routes

import com.example.dao.Course
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.CourseInterfaceImpl
import com.example.plugins.CourseNotFoundException
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

fun Application.configureCourseRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val courseNameMinLength = config.property("ktor.CourseValidation.courseNameMinLength").getString()?.toIntOrNull()
    val courseNameMaxLength = config.property("ktor.CourseValidation.courseNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.COURSE) {
            val courseInterfaceImpl: CourseInterfaceImpl by inject()

            get {
                handleGetCourses(courseInterfaceImpl)
            }

            post {
                handlePostCourse(call, courseInterfaceImpl,courseNameMinLength,courseNameMaxLength)
            }

            get("/{id?}") {
                handleGetCourseById(call, courseInterfaceImpl)
            }

            delete("/{id?}") {
                handleDeleteCourse(call, courseInterfaceImpl)
            }

            put("/{id?}") {
                handlePutCourse(call, courseInterfaceImpl)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetCourses(courseInterfaceImpl: CourseInterfaceImpl) {
    val courses = courseInterfaceImpl.getAllCourses()
    if (courses.isEmpty()) {
        throw CourseNotFoundException()
    } else {
        application.environment.log.info("All course details")
        call.respond(courses)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostCourse(
    call: ApplicationCall,
    courseInterfaceImpl: CourseInterfaceImpl,
    courseNameMinLength: Int?,
    courseNameMaxLength: Int?
) {
    val courses = call.receive<Course>()

    if (courseNameMinLength != null && courseNameMaxLength != null) {
        if (courses.name.length in courseNameMinLength..courseNameMaxLength) {
            val insert = courseInterfaceImpl.insertCourse(courses.studentId, courses.name)
            if (insert != null) {
                application.environment.log.info("Course is created")
                call.respond(HttpStatusCode.Created, insert)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond("Invalid Length")
        }
    } else {
        call.respond(HttpStatusCode.InternalServerError, "Course name validation configuration is missing")
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleDeleteCourse(
    call: ApplicationCall,
    courseInterfaceImpl: CourseInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val delCourse = courseInterfaceImpl.deleteCourse(id)
        if (delCourse) {
            application.environment.log.info("Course is deleted")
            call.respond(HttpStatusCode.OK)
        } else {
            application.environment.log.error("No course is found with given id")
            throw CourseNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePutCourse(
    call: ApplicationCall,
    courseInterfaceImpl: CourseInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val course = call.receive<Course>()
        val editCourse = courseInterfaceImpl.editCourse(course.id, course.name)
        if (editCourse) {
            application.environment.log.info("Course is updated")
            call.respond(HttpStatusCode.OK)
        } else {
            throw CourseNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}
private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetCourseById(
    call: ApplicationCall,
    courseInterfaceImpl: CourseInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val fetchedCourse = courseInterfaceImpl.getCourseById(id)
        if (fetchedCourse != null) {
            application.environment.log.info("Course is found with given id")
            call.respond(fetchedCourse)
        } else {
            throw CourseNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}
