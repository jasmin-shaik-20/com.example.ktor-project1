package com.example.dao

import org.jetbrains.exposed.sql.Table

object StudentCourses : Table() {
    val studentId = integer("studentId") references (Students.id)
    val courseId = integer("courseId") references (Courses.id)
    override val primaryKey = PrimaryKey(studentId, courseId)
}
