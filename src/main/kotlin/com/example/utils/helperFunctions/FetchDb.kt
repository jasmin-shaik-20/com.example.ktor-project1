package com.example.utils.helperFunctions

import com.example.database.table.*
import com.example.model.*
import org.jetbrains.exposed.sql.ResultRow

fun resultRowToUser(row: ResultRow) =
    User(
        id = row[Users.id].toString(),
        name = row[Users.name]
    )

fun resultRowToStudent(row: ResultRow) =
    Student(row[Students.id].toString(),
        row[Students.name]
    )

fun resultRowToCourse(row: ResultRow) =
    Course(row[Courses.id].toString(),
        row[Courses.studentId].toString(),
        row[Courses.name]
    )

fun resultRowToProfile(row: ResultRow) =
    UserProfile(
        id= row[UserProfiles.id].toString(),
        userId = row[UserProfiles.userId].toString(),
        email = row[UserProfiles.email],
        age = row[UserProfiles.age]
    )

fun resultRowToProduct(row: ResultRow)=
    Product(row[Products.id].toString(),
        row[Products.userId].toString(),
        row[Products.name],
        row[Products.price]
    )

