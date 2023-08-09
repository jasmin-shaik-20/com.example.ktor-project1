package com.example.routes

import com.example.dao.Course
import com.example.file.ApiEndPoint
import com.example.interfaceimpl.CourseInterfaceImpl
import com.example.plugins.CourseNotFoundException
import com.example.plugins.InvalidIDException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCourseRoutes(){
    routing {
        route(ApiEndPoint.COURSE){
            val courseInterfaceImpl= CourseInterfaceImpl()
            get{
                val courses=courseInterfaceImpl.getAllCourses()
                if(courses!=null){
                    call.respond(courses)
                }
                else{
                    throw Throwable()
                }
            }

            post{
                val courses=call.receive<Course>()
                val insert=courseInterfaceImpl.insertCourse(courses.studentId,courses.name)
                if(insert!=null){
                    call.respond(insert)
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?:return@get throw InvalidIDException()
                val fetid=courseInterfaceImpl.getCourseById(id.toInt())
                if(fetid!=null){
                    call.respond(fetid)
                }
                else{
                    throw CourseNotFoundException()
                }
            }

        }
    }
}