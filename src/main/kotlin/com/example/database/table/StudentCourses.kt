package com.example.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object StudentCourses : UUIDTable("student courses") {
    val studentId = reference("studentId",Students)
    val courseId = reference("courseId",Courses)
}
