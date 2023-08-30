package com.example.services

import com.example.dao.Student
import com.example.plugins.StudentNotFoundException
import com.example.repository.StudentInterfaceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class StudentServices {
    suspend fun handleGetStudents(call: ApplicationCall,studentInterfaceImpl: StudentInterfaceImpl) {
        val students = studentInterfaceImpl.getAllStudents()
        if (students.isEmpty()) {
            throw StudentNotFoundException()
        } else {
            call.application.environment.log.info("All student details")
            call.respond(students)
        }
    }

    suspend fun handlePostStudent(
        call: ApplicationCall,
        studentInterfaceImpl: StudentInterfaceImpl,
        studentNameMinLength: Int?,
        studentNameMaxLength: Int?
    ) {
        val students = call.receive<Student>()
        if (students.name.length in studentNameMinLength!!..studentNameMaxLength!!) {
            val student = studentInterfaceImpl.insertStudent(students.id, students.name)
            if (student != null) {
                call.application.environment.log.info("Student is created $student")
                call.respond(HttpStatusCode.Created, student)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond("Invalid Length")
        }
    }

    suspend fun handleGetStudentById(
        call: ApplicationCall,
        studentInterfaceImpl: StudentInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        val fetid = studentInterfaceImpl.getStudentById(id!!.toInt())
        if (fetid != null) {
            call.application.environment.log.info("Student is found")
            call.respond(fetid)
        } else {
            throw StudentNotFoundException()
        }
    }

    suspend fun handleDeleteStudent(
        call: ApplicationCall,
        studentInterfaceImpl: StudentInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val delStudent = studentInterfaceImpl.deleteStudent(id)
            if (delStudent) {
                call.application.environment.log.info("Student is deleted")
                call.respond(HttpStatusCode.OK)
            } else {
                call.application.environment.log.error("No student found with given id")
                throw StudentNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    suspend fun handlePutStudent(
        call: ApplicationCall,
        studentInterfaceImpl: StudentInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val student = call.receive<Student>()
            val editStudent = studentInterfaceImpl.editStudent(student.id, student.name)
            if (editStudent) {
                call.application.environment.log.info("Student is updated")
                call.respond(HttpStatusCode.OK)
            } else {
                throw StudentNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}