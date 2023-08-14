package com.example.plugins

import com.example.endpoints.ApiEndPoint
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureCallLogging(){
    install(CallLogging){
        level=Level.INFO
        filter { call->
            call.request.path().startsWith(ApiEndPoint.USER)
            call.request.path().startsWith(ApiEndPoint.USERPROFILE)
            call.request.path().startsWith(ApiEndPoint.PRODUCT)
            call.request.path().startsWith(ApiEndPoint.STUDENT)
            call.request.path().startsWith(ApiEndPoint.COURSE)
            call.request.path().startsWith(ApiEndPoint.STUDENTCOURSES)
        }
    }
}