package com.example.routes

import com.example.dao.Student
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.StudentInterfaceImpl
import com.example.plugins.StudentNotFoundException
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.application
import io.ktor.server.application.call
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

fun Application.configureStudentRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val studentNameMinLength = config.property("ktor.StudentValidation.studentNameMinLength").getString()?.toIntOrNull()
    val studentNameMaxLength = config.property("ktor.StudentValidation.studentNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.STUDENT) {
            val studentInterfaceImpl: StudentInterfaceImpl by inject()

            get {
                handleGetStudents(studentInterfaceImpl)
            }

            post {
                handlePostStudent(call, studentInterfaceImpl, studentNameMinLength, studentNameMaxLength)
            }

            get("/{id?}") {
                handleGetStudentById(call, studentInterfaceImpl)
            }

            delete("/{id?}") {
                handleDeleteStudent(call, studentInterfaceImpl)
            }

            put("/{id?}") {
                handlePutStudent(call, studentInterfaceImpl)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.
        handleGetStudents(studentInterfaceImpl: StudentInterfaceImpl) {
    val students = studentInterfaceImpl.getAllStudents()
    if (students.isEmpty()) {
        throw StudentNotFoundException()
    } else {
        application.environment.log.info("All student details")
        call.respond(students)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostStudent(
    call: ApplicationCall,
    studentInterfaceImpl: StudentInterfaceImpl,
    studentNameMinLength: Int?,
    studentNameMaxLength: Int?
) {
    val students = call.receive<Student>()
    if (students.name.length in studentNameMinLength!!..studentNameMaxLength!!) {
        val student = studentInterfaceImpl.insertStudent(students.id, students.name)
        if (student != null) {
            application.environment.log.info("Student is created $student")
            call.respond(HttpStatusCode.Created, student)
        } else {
            call.respond(HttpStatusCode.InternalServerError)
        }
    } else {
        call.respond("Invalid Length")
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetStudentById(
    call: ApplicationCall,
    studentInterfaceImpl: StudentInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    val fetid = studentInterfaceImpl.getStudentById(id!!.toInt())
    if (fetid != null) {
        application.environment.log.info("Student is found")
        call.respond(fetid)
    } else {
        throw StudentNotFoundException()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleDeleteStudent(
    call: ApplicationCall,
    studentInterfaceImpl: StudentInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val delStudent = studentInterfaceImpl.deleteStudent(id)
        if (delStudent) {
            application.environment.log.info("Student is deleted")
            call.respond(HttpStatusCode.OK)
        } else {
            application.environment.log.error("No student found with given id")
            throw StudentNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePutStudent(
    call: ApplicationCall,
    studentInterfaceImpl: StudentInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val student = call.receive<Student>()
        val editStudent = studentInterfaceImpl.editStudent(student.id, student.name)
        if (editStudent) {
            application.environment.log.info("Student is updated")
            call.respond(HttpStatusCode.OK)
        } else {
            throw StudentNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}

