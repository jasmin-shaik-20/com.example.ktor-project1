package com.example.routes

import com.example.dao.Course
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.CourseInterfaceImpl
import com.example.plugins.CourseNotFoundException
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureCourseRoutes(){
    val config = HoconApplicationConfig(ConfigFactory.load())
    val courseNameMinLength= config.property("ktor.CourseValidation.courseNameMinLength").getString()?.toIntOrNull()
    val courseNameMaxLength= config.property("ktor.CourseValidation.courseNameMaxLength").getString()?.toIntOrNull()
    routing {
        route(ApiEndPoint.COURSE){
            val courseInterfaceImpl : CourseInterfaceImpl by inject()
            get{
                val courses=courseInterfaceImpl.getAllCourses()
                if(courses.isEmpty()){
                    throw CourseNotFoundException()
                }
                else{
                    call.application.environment.log.info("All course details")
                    call.respond(courses)
                }
            }

            post{
                val courses=call.receive<Course>()
                if(courses.name.length in courseNameMinLength!!..courseNameMaxLength!!) {
                    val insert = courseInterfaceImpl.insertCourse(courses.studentId, courses.name)
                    if (insert != null) {
                        call.application.environment.log.info("Course is created")
                        call.respond(HttpStatusCode.Created, insert)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
                else{
                    call.respond("Invalid Length")
                }
            }

            get("/{id?}"){
                val id=call.parameters["id"]?.toIntOrNull()
                val fetid=courseInterfaceImpl.getCourseById(id!!.toInt())
                if(fetid!=null){
                    call.application.environment.log.info("Course is found with given id")
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
                        call.application.environment.log.info("Course is deleted")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.application.environment.log.error("No course is found with given id")
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
                        call.application.environment.log.info("Course is updated")
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