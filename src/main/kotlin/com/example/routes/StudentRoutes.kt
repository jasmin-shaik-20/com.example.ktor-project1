package com.example.routes

import com.example.dao.Student
import com.example.dao.User
import com.example.file.ApiEndPoint
import com.example.interfaceimpl.StudentInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.StudentNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStudentRoutes(){
    routing {
        route(ApiEndPoint.STUDENT){
            val studentInterfaceImpl= StudentInterfaceImpl()
            get {
                val students = studentInterfaceImpl.getAllStudents()
                if (students.isEmpty()) {
                    call.respond("No students found")
                }
                else{
                    call.respond(students)
                }
            }

            post {
                val students = call.receive<Student>()
                val student = studentInterfaceImpl.insertStudent(students.id, students.name)
                if (student != null) {
                    call.respond(student)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val fetid=studentInterfaceImpl.getStudentById(id.toInt())
                if(fetid!=null){
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
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.respond(HttpStatusCode.NotFound)
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
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

    }
}