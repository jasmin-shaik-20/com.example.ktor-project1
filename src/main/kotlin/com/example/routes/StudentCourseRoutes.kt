package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.StudentCourseInterfaceImpl
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Application.configureStudentCourseRoutes(){
    routing{
        route(ApiEndPoint.STUDENTCOURSES){
            val studentCourseInterfaceImpl : StudentCourseInterfaceImpl by inject()
            get("/student/{id?}"){
                val id=call.parameters["id"]?.toIntOrNull()
                val courses=studentCourseInterfaceImpl.getCoursesStudentId(id!!.toInt())
                if(courses.isNotEmpty()){
                    call.application.environment.log.info("Courses found")
                    call.respond(courses)
                }
                else{
                    call.respond("No courses found with given id")
                }
            }

            get("/course/{id?}"){
                val id=call.parameters["id"]?:return@get call.respond("Invalid id")
                val students=studentCourseInterfaceImpl.getStudentsCourseId(id.toInt())
                if(students.isNotEmpty()){
                    call.application.environment.log.info("Students found")
                    call.respond(students)
                }
                else{
                    call.respond("No students found with given courseId")
                }
            }
        }
    }
}
