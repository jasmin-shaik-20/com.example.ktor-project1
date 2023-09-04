package com.example.repository

import com.example.dao.StudentCourseRepositoryDao
import com.example.database.table.*
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.rowToCourse
import com.example.utils.helperFunctions.rowToStudent
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class StudentCourseRepositoryImpl : StudentCourseRepositoryDao {
    override suspend fun getCoursesStudentId(id: Int): List<Course> = dbQuery{
        return@dbQuery transaction {
            (Courses innerJoin StudentCourses).
            select{ StudentCourses.studentId eq id}.
            map(::rowToCourse)
        }
    }
    override suspend fun getStudentsCourseId(id: Int): List<Student> = dbQuery {
        return@dbQuery transaction {
            (Students innerJoin StudentCourses).select {
                StudentCourses.courseId eq id
            }.map(::rowToStudent)
        }
    }

}




