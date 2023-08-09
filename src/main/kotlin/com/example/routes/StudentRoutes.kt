package com.example.routes

import com.example.dao.Student
import com.example.interfaceimpl.StudentInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.StudentNotFoundException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStudentRoutes(){
    routing {
        route("/student"){
            val studentInterfaceImpl= StudentInterfaceImpl()
            get {
                val students = studentInterfaceImpl.getAllStudents()
                if (students != null) {
                    call.respond(students)
                }
                else{
                    throw Throwable()
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
        }

    }
}