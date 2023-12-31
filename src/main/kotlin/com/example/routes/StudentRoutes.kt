package com.example.routes
import com.example.config.StudentConfig.studentNameMaxLength
import com.example.config.StudentConfig.studentNameMinLength
import com.example.entities.StudentEntity
import com.example.model.Student
import com.example.model.StudentInput
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import com.example.services.StudentServices
import com.example.utils.appConstants.ApiEndPoints
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureStudentRoutes() {

    val studentServices: StudentServices by inject()

    routing {
        route(ApiEndPoints.STUDENT) {
            get {
                val students = studentServices.handleGetStudents()
                call.respond(students)
                call.application.environment.log.info("Returned a list of students")
            }

            post {
                val studentDetails = call.receive<StudentInput>()
                val student = studentServices.handlePostStudent(
                    studentDetails, studentNameMinLength, studentNameMaxLength)
                call.respond(HttpStatusCode.Created, student)
                call.application.environment.log.info("Created a new student: $student")
            }

            get("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@get call.respond("Missing id")
                val student = studentServices.handleGetStudentById(id)
                call.respond(student)
                call.application.environment.log.info("Returned student with ID: $id")
            }

            delete("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@delete call.respond("Missing id")
                studentServices.handleDeleteStudent(id)
                call.respond(HttpStatusCode.OK, "Student deleted successfully")
                call.application.environment.log.info("Deleted student with ID: $id")
            }

            put("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?:return@put call.respond("Missing id")
                val studentDetails = call.receive<Student>()
                studentServices.handleUpdateStudent(id, studentDetails)
                call.respond(HttpStatusCode.OK, "Student updated successfully")
                call.application.environment.log.info("Updated student with ID: $id")
            }
        }
    }
}
