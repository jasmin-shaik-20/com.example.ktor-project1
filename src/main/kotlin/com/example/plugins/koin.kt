package com.example.plugins

import com.example.repository.UsersInterfaceImpl
import com.example.repository.ProfileInterfaceImpl
import com.example.repository.ProductInterfaceImpl
import com.example.repository.StudentInterfaceImpl
import com.example.repository.CourseInterfaceImpl
import com.example.repository.StudentCourseInterfaceImpl
import com.example.repository.PersonInterfaceImpl
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
