package com.example.di

import CourseRepositoryImpl
import com.example.dao.UserDao
import UserProfileDao
import com.example.dao.*
import com.example.repository.*
import com.example.services.UserServices
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val appModule=module{
    singleOf(::UsersRepositoryImpl) {bind<UserDao>()}
    singleOf(::ProfileRepositoryImpl){ bind<UserProfileDao>() }
    singleOf(::ProductRepositoryImpl){ bind<ProductDao>() }
    singleOf(::StudentRepositoryImpl){ bind<StudentDao>() }
    singleOf(::CourseRepositoryImpl){ bind<CourseDao>() }
    singleOf(::StudentCourseRepositoryImpl){ bind<StudentCourseDao>() }
    singleOf(::PersonRepositoryImpl){ bind<PersonDao>() }
    single { UserServices() }
}