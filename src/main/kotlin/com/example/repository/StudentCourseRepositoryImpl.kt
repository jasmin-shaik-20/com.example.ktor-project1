package com.example.repository

import com.example.dao.StudentCourseDao
import com.example.database.table.Courses
import com.example.database.table.StudentCourses
import com.example.database.table.Students
import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import com.example.model.Course
import com.example.model.Student
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToCourse
import com.example.utils.helperFunctions.resultRowToStudent
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class StudentCourseRepositoryImpl : StudentCourseDao {

    override suspend fun getCoursesByStudentId(studentId: UUID): List<Course> = dbQuery{
        return@dbQuery transaction {
            (Courses innerJoin StudentCourses).select(StudentCourses.studentId eq studentId).map(::resultRowToCourse)
        }

    }

    override suspend fun getStudentsByCourseId(courseId: UUID): List<Student> = dbQuery{
        return@dbQuery transaction {
            (Students innerJoin StudentCourses).select(StudentCourses.courseId eq courseId).map(::resultRowToStudent)
        }
    }
}
