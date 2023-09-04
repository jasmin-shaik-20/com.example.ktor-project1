package com.example.utils.helperFunctions

import com.example.dao.*
import com.example.database.table.*
import org.jetbrains.exposed.sql.ResultRow

fun resultRowToUser(row: ResultRow) =
    User(id = row[Users.id],
        name = row[Users.name]
    )

fun rowToStudent(row: ResultRow) =
    Student(row[Students.id],
        row[Students.name]
    )

fun rowToCourse(row: ResultRow) =
    Course(row[Courses.id],
        row[Courses.studentId],
        row[Courses.name]
    )

fun resultRowToProfile(row: ResultRow) =
    UserProfile(
        profileId = row[Profile.profileId],
        userId = row[Profile.userId],
        email = row[Profile.email],
        age = row[Profile.age]
    )

fun rowToProduct(row: ResultRow)=
    Product(row[Products.productId],
        row[Products.userId],
        row[Products.name],
        row[Products.price]
    )

fun rowToPerson(row: ResultRow) =
    Person(row[Persons.id],
        row[Persons.name]
    )
