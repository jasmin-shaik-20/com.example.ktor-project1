package com.example.plugins

import com.example.utils.appConstants.ApiEndPoints
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.configureCallLogging(){
    install(CallLogging){
        level=Level.INFO
        filter { call->
            call.request.path().startsWith(ApiEndPoints.USER)
            call.request.path().startsWith(ApiEndPoints.USERPROFILE)
            call.request.path().startsWith(ApiEndPoints.PRODUCT)
            call.request.path().startsWith(ApiEndPoints.STUDENT)
            call.request.path().startsWith(ApiEndPoints.COURSE)
            call.request.path().startsWith(ApiEndPoints.STUDENTCOURSES)
        }
    }
}
