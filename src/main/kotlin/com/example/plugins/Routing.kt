package com.example.plugins

import com.example.routes.configureImageRoutes
import com.example.routes.configureCourseRoutes

import com.example.routes.configureProductRoutes
import com.example.routes.configureUserProfile
import com.example.routes.configureStudentCourseRoutes
import com.example.routes.configureUserSession
import configureCustomerRoutes
import configureLoginRoutes
import com.example.routes.configureStudentRoutes
import com.example.routes.configureUserRoutes
import io.ktor.server.application.Application

fun Application.configureRouting() {

    configureUserRoutes()
    configureUserProfile()
    configureProductRoutes()
    configureStudentRoutes()
    configureCustomerRoutes()
    configureCourseRoutes()
    configureCallLogging()
    configureImageRoutes()
    configureLoginRoutes()
    configureStudentCourseRoutes()
    configureUserSession()

}
