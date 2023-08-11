package com.example.plugins

import com.example.routes.*
import io.ktor.server.application.*

fun Application.configureRouting() {

    configureCustomerRoutes()
    configureUserRoutes()
    configureUserProfile()
    configureProductRoutes()
    configureStudentRoutes()
    configureCourseRoutes()
    configureCallLogging()
    configureImageRoutes()
    configurePersonRoutes()
    configureLoginRoutes()
    configureStudentCourseRoutes()
    configureUserSession()

}
