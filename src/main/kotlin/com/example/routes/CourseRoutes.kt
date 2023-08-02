package com.example.routes

import com.example.dao.course
import com.example.interfaces.CourseInterfaceImpl
import com.example.plugins.CourseNotFoundException
import com.example.plugins.InvalidIDException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCourseRoutes(){
    routing {
        route("/course"){
            val courseInterfaceImpl= CourseInterfaceImpl()
            get{
                val getcourses=courseInterfaceImpl.getAllCourses()
                if(getcourses!=null){
                    call.respond(getcourses)
                }
                else{
                    throw Throwable()
                }
            }

            post{
                val courses=call.receive<course>()
                val insert=courseInterfaceImpl.insertCourse(courses.student_id,courses.name)
                if(insert!=null){
                    call.respond(insert)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val getid=courseInterfaceImpl.getCourseById(id.toInt())
                if(getid!=null){
                    call.respond(getid)
                }
                else{
                    throw CourseNotFoundException()
                }
            }

        }
    }
}