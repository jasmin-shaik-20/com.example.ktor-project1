package com.example.di

import CourseRepositoryImpl
import com.example.dao.UserDao
import UserProfileDao
import com.example.dao.*
import com.example.database.table.StudentCourses
import com.example.repository.*
import com.example.services.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.math.sin


val appModule=module{
    singleOf(::UsersRepositoryImpl) {bind<UserDao>()}
    singleOf(::ProfileRepositoryImpl){ bind<UserProfileDao>() }
    singleOf(::ProductRepositoryImpl){ bind<ProductDao>() }
    singleOf(::StudentRepositoryImpl){ bind<StudentDao>() }
    singleOf(::CourseRepositoryImpl){ bind<CourseDao>() }
    singleOf(::StudentCourseRepositoryImpl){ bind<StudentCourseDao>() }
    single { UserServices() }
    single { UserProfileServices()}
    single { ProductServices()}
    single { StudentServices()}
    single { CourseServices()}
    single { StudentCourseServices()}
}