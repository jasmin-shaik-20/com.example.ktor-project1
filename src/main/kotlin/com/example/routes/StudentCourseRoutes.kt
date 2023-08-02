package com.example.routes

import com.example.interfaces.StudentCourseInterfaceImpl
import com.example.plugins.InvalidIDException
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStudentCourseRoutes(){
    routing{
        route("/studentcourse"){
            val studentCourseInterfaceImpl= StudentCourseInterfaceImpl()
            get("/student/{id?}"){
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val courses=studentCourseInterfaceImpl.getCoursesBystudentId(id.toInt())
                if(courses!=null){
                    call.respond(courses)
                }
            }

            get("/course/{id?}"){
                val id=call.parameters["id"]?:return@get call.respond("Invalid id")
                val students=studentCourseInterfaceImpl.getStudentsBycourseId(id.toInt())
                if(students!=null){
                    call.respond(students)
                }
            }

        }
    }
}