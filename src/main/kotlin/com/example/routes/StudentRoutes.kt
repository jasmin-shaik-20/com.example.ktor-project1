package com.example.routes

import com.example.dao.Student
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.StudentInterfaceImpl
import com.example.plugins.StudentNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureStudentRoutes(){
    routing {
        route(ApiEndPoint.STUDENT){
            val studentInterfaceImpl : StudentInterfaceImpl by inject()
            get {
                val students = studentInterfaceImpl.getAllStudents()
                if (students.isEmpty()) {
                    throw StudentNotFoundException()
                }
                else{
                    call.application.environment.log.info("All student details")
                    call.respond(students)
                }
            }

            post {
                val students = call.receive<Student>()
                val student = studentInterfaceImpl.insertStudent(students.id, students.name)
                if (student != null) {
                    call.application.environment.log.info("Student is created $student")
                    call.respond(HttpStatusCode.Created,student)
                }
                else{
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?.toIntOrNull()
                val fetid=studentInterfaceImpl.getStudentById(id!!.toInt())
                if(fetid!=null){
                    call.application.environment.log.info("Student is found")
                    call.respond(fetid)
                }
                else{
                    throw StudentNotFoundException()
                }
            }

            delete("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val delStudent=studentInterfaceImpl.deleteStudent(id)
                    if(delStudent){
                        call.application.environment.log.info("Student is deleted")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.application.environment.log.error("No student found with given id")
                        throw StudentNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val student=call.receive<Student>()
                    val editStudent=studentInterfaceImpl.editStudent(student.id,student.name)
                    if(editStudent){
                        call.application.environment.log.info("Student is updated")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw StudentNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

    }
}