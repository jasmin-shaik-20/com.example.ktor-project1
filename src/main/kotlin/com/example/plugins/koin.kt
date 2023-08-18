package com.example.plugins

import UsersInterface
import com.example.interfaceimpl.UsersInterfaceImpl
import com.example.interfaceimpl.ProfileInterfaceImpl
import com.example.interfaceimpl.ProductInterfaceImpl
import com.example.interfaceimpl.StudentInterfaceImpl
import com.example.interfaceimpl.CourseInterfaceImpl
import com.example.interfaceimpl.StudentCourseInterfaceImpl
import com.example.interfaceimpl.PersonInterfaceImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
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
