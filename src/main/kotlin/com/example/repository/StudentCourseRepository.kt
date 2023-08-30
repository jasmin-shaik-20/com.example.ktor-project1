package com.example.repository

import com.example.dao.Course
import com.example.dao.Courses
import com.example.dao.StudentCourses
import com.example.dao.Student
import com.example.dao.Students
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class StudentCourseRepository {
    suspend fun getCoursesStudentId(id: Int): List<Course> = dbQuery{
        return@dbQuery transaction {
            (Courses innerJoin StudentCourses).
            select{ StudentCourses.studentId eq id}.
            map(::rowToCourse)
        }
    }
    suspend fun getStudentsCourseId(id: Int): List<Student> = dbQuery {
        return@dbQuery transaction {
            (Students innerJoin StudentCourses).select {
                StudentCourses.courseId eq id
            }.map(::rowToStudent)
        }
    }
}

private fun rowToStudent(row: ResultRow) =
    Student(row[Students.id], row[Students.name])

private fun rowToCourse(row: ResultRow) =
    Course(row[Courses.id], row[Courses.studentId], row[Courses.name])
