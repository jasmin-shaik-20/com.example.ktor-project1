package com.example.routes

import com.example.dao.Course
import com.example.dao.Student
import com.example.file.ApiEndPoint
import com.example.interfaceimpl.CourseInterfaceImpl
import com.example.plugins.CourseNotFoundException
import com.example.plugins.InvalidIDException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureCourseRoutes(){
    routing {
        route(ApiEndPoint.COURSE){
            val courseInterfaceImpl : CourseInterfaceImpl by inject()
            get{
                val courses=courseInterfaceImpl.getAllCourses()
                if(courses.isEmpty()){
                    throw CourseNotFoundException()
                }
                else{
                    call.respond(courses)
                }
            }

            post{
                val courses=call.receive<Course>()
                val insert=courseInterfaceImpl.insertCourse(courses.studentId,courses.name)
                if(insert!=null){
                    call.respond(HttpStatusCode.Created,insert)
                }
                else{
                    call.respond(HttpStatusCode.InternalServerError)
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

            delete("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val delCourse=courseInterfaceImpl.deleteCourse(id)
                    if(delCourse){
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw CourseNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val course=call.receive<Course>()
                    val editCourse=courseInterfaceImpl.editCourse(course.id,course.name)
                    if(editCourse){
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw CourseNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

        }
    }
}