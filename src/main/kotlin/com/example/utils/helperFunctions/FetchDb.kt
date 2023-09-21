package com.example.utils.helperFunctions

import com.example.database.table.*
import com.example.model.User
import com.example.model.UserProfile
import org.jetbrains.exposed.sql.ResultRow

fun resultRowToUser(row: ResultRow) =
    User(
        id = row[Users.id].toString(),
        name = row[Users.name]
    )

//fun rowToStudent(row: ResultRow) =
//    Student(row[Students.id],
//        row[Students.name]
//    )
//
//fun rowToCourse(row: ResultRow) =
//    Course(row[Courses.id],
//        row[Courses.studentId],
//        row[Courses.name]
//    )
//
fun resultRowToProfile(row: ResultRow) =
    UserProfile(
        id= row[UserProfiles.id].toString(),
        userId = row[UserProfiles.userId].toString(),
        email = row[UserProfiles.email],
        age = row[UserProfiles.age]
    )
//
//fun rowToProduct(row: ResultRow)=
//    Product(row[Products.productId],
//        row[Products.userId],
//        row[Products.name],
//        row[Products.price]
//    )
//
//fun rowToPerson(row: ResultRow) =
//    Person(row[Persons.id],
//        row[Persons.name]
//    )
