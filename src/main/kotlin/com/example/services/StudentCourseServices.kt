package com.example.services

import com.example.repository.StudentCourseRepository
import io.ktor.server.application.*
import io.ktor.server.response.*

class StudentCourseServices {
    suspend fun handleGetCoursesByStudentId(call: ApplicationCall, studentCourseRepository: StudentCourseRepository) {
        val id = call.parameters["id"]?.toIntOrNull()
        val courses = studentCourseRepository.getCoursesStudentId(id!!.toInt())
        if (courses.isNotEmpty()) {
            call.application.environment.log.info("Courses found")
            call.respond(courses)
        } else {
            call.respond("No courses found with the given id")
        }
    }

    suspend fun handleGetStudentsByCourseId(call: ApplicationCall, studentCourseRepository: StudentCourseRepository) {
        val id = call.parameters["id"] ?: return call.respond("Invalid id")
        val students = studentCourseRepository.getStudentsCourseId(id.toInt())
        if (students.isNotEmpty()) {
            call.application.environment.log.info("Students found")
            call.respond(students)
        } else {
            call.respond("No students found with the given courseId")
        }
    }

}