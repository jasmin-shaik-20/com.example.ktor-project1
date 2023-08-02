package com.example.routes

import com.example.dao.student
import com.example.interfaces.StudentInterfaceImpl
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
                val getstudents = studentInterfaceImpl.getAllStudents()
                if (getstudents != null) {
                    call.respond(getstudents)
                }
                else{
                    throw Throwable()
                }
            }

            post {
                val students = call.receive<student>()
                val poststudent = studentInterfaceImpl.insertStudent(students.id, students.name)
                if (poststudent != null) {
                    call.respond(poststudent)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val getid=studentInterfaceImpl.getStudentById(id.toInt())
                if(getid!=null){
                    call.respond(getid)
                }
                else{
                    throw StudentNotFoundException()
                }
            }
        }

    }
}