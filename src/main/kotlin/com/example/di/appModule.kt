package com.example.di

import com.example.repository.*
import org.koin.dsl.module

val appModule=module{
    single<UsersRepositoryImpl>{ UsersRepositoryImpl() }
    single<ProfileRepositoryImpl>{ ProfileRepositoryImpl() }
    single<ProductRepositoryImpl>{ ProductRepositoryImpl() }
    single<StudentRepositoryImpl>{ StudentRepositoryImpl() }
    single<CourseRepository>{ CourseRepository() }
    single<StudentCourseRepository>{ StudentCourseRepository() }
    single<PersonRepository>{ PersonRepository() }
}