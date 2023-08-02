package com.example.plugins

import com.example.dao.*
import com.example.interfaces.CourseInterfaceImpl
import com.example.interfaces.ProductInterfaceImpl
import com.example.interfaces.StudentCourseInterfaceImpl
import com.example.interfaces.StudentInterfaceImpl
import com.example.routes.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    configureCustomerRoutes()
    configureUserRoutes()
    configureUserProfile()
    configureProductRoutes()
    configureStudentRoutes()
    configureCourseRoutes()
    configureLoginRoutes()
    configureStudentCourseRoutes()
    configureUserSession()













}
