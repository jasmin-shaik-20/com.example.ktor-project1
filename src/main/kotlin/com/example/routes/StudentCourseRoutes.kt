package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.repository.StudentCourseInterfaceImpl
import com.example.services.StudentCourseServices
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
            val studentCourseServices: StudentCourseServices by inject()
            get("/student/{id?}"){
                studentCourseServices.handleGetStudentsByCourseId(call,studentCourseInterfaceImpl)
            }

            get("/course/{id?}"){
                studentCourseServices.handleGetCoursesByStudentId(call,studentCourseInterfaceImpl)
            }
        }
    }
}
