package com.example.plugins

import com.example.repository.UsersRepository
import com.example.repository.ProfileRepository
import com.example.repository.ProductRepository
import com.example.repository.StudentRepository
import com.example.repository.CourseRepository
import com.example.repository.StudentCourseRepository
import com.example.repository.PersonRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(){
    val appModule=module{
        single<UsersRepository>{UsersRepository()}
        single<ProfileRepository>{ProfileRepository()}
        single<ProductRepository>{ProductRepository()}
        single<StudentRepository>{StudentRepository()}
        single<CourseRepository>{CourseRepository()}
        single<StudentCourseRepository>{StudentCourseRepository()}
        single<PersonRepository>{PersonRepository()}
    }
    install(Koin){
        modules(appModule)
    }
}
