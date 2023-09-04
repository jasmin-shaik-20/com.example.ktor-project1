package com.example.plugins

import com.example.repository.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(){
    val appModule=module{
        single<UsersRepositoryImpl>{UsersRepositoryImpl()}
        single<ProfileRepositoryImpl>{ProfileRepositoryImpl()}
        single<ProductRepositoryImpl>{ProductRepositoryImpl()}
        single<StudentRepositoryImpl>{StudentRepositoryImpl()}
        single<CourseRepository>{CourseRepository()}
        single<StudentCourseRepository>{StudentCourseRepository()}
        single<PersonRepository>{PersonRepository()}
    }
    install(Koin){
        modules(appModule)
    }
}
