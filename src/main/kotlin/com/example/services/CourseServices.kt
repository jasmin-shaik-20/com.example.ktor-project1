package com.example.services

import com.example.dao.Course
import com.example.plugins.CourseNotFoundException
import com.example.repository.CourseInterfaceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class CourseServices {
    suspend fun handleGetCourses(call: ApplicationCall,courseInterfaceImpl: CourseInterfaceImpl) {
        val courses = courseInterfaceImpl.getAllCourses()
        if (courses.isEmpty()) {
            throw CourseNotFoundException()
        } else {
            call.application.environment.log.info("All course details")
            call.respond(courses)
        }
    }

    suspend fun handlePostCourse(
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
                    call.application.environment.log.info("Course is created")
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

    suspend fun handleDeleteCourse(
        call: ApplicationCall,
        courseInterfaceImpl: CourseInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val delCourse = courseInterfaceImpl.deleteCourse(id)
            if (delCourse) {
                call.application.environment.log.info("Course is deleted")
                call.respond(HttpStatusCode.OK)
            } else {
                call.application.environment.log.error("No course is found with given id")
                throw CourseNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    suspend fun handlePutCourse(
        call: ApplicationCall,
        courseInterfaceImpl: CourseInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val course = call.receive<Course>()
            val editCourse = courseInterfaceImpl.editCourse(course.id, course.name)
            if (editCourse) {
                call.application.environment.log.info("Course is updated")
                call.respond(HttpStatusCode.OK)
            } else {
                throw CourseNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
    suspend fun handleGetCourseById(
        call: ApplicationCall,
        courseInterfaceImpl: CourseInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val fetchedCourse = courseInterfaceImpl.getCourseById(id)
            if (fetchedCourse != null) {
                call.application.environment.log.info("Course is found with given id")
                call.respond(fetchedCourse)
            } else {
                throw CourseNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

}