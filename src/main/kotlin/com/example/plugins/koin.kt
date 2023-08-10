package com.example.plugins

import UsersInterface
import com.example.interfaceimpl.*
import com.example.interfaces.*
import com.example.module
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(){
    val appModule=module{
        single<UsersInterfaceImpl>{UsersInterfaceImpl()}
        single<ProfileInterfaceImpl>{ProfileInterfaceImpl()}
        single<ProductInterfaceImpl>{ProductInterfaceImpl()}
        single<StudentInterfaceImpl>{StudentInterfaceImpl()}
        single<CourseInterfaceImpl>{CourseInterfaceImpl()}
        single<StudentCourseInterfaceImpl>{StudentCourseInterfaceImpl()}
        single<PersonInterfaceImpl>{PersonInterfaceImpl()}
    }
    install(Koin){
        modules(appModule)
    }
}