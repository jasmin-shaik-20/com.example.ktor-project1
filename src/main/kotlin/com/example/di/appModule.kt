package com.example.di

import com.example.repository.*
import org.koin.dsl.module

val appModule=module{
    single<UsersRepositoryImpl>{ UsersRepositoryImpl() }
    single<ProfileRepositoryImpl>{ ProfileRepositoryImpl() }
    single<ProductRepositoryImpl>{ ProductRepositoryImpl() }
    single<StudentRepositoryImpl>{ StudentRepositoryImpl() }
    single<CourseRepositoryImpl>{ CourseRepositoryImpl() }
    single<StudentCourseRepositoryImpl>{ StudentCourseRepositoryImpl() }
    single<PersonRepositoryImpl>{ PersonRepositoryImpl() }
}