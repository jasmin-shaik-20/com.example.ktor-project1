package com.example.plugins

import com.example.routes.configureImageRoutes
import com.example.routes.configureLoginRoutes
import com.example.routes.configureCourseRoutes
import com.example.routes.configureStudentRoutes
import com.example.routes.configureProductRoutes
import com.example.routes.configureUserProfile
import com.example.routes.configureUserRoutes
import com.example.routes.configureCustomerRoutes
import com.example.routes.configurePersonRoutes
import com.example.routes.configureStudentCourseRoutes
import com.example.routes.configureUserSession
import io.ktor.server.application.Application

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
