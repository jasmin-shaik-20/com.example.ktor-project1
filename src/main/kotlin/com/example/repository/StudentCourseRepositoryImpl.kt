package com.example.repository

import com.example.dao.StudentCourseDao
import com.example.database.table.Courses
import com.example.database.table.StudentCourses
import com.example.database.table.Students
import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class StudentCourseRepositoryImpl : StudentCourseDao {

    override suspend fun getCoursesByStudentId(studentId: UUID): List<CourseEntity> {
        return transaction {
            (Courses innerJoin StudentCourses)
                .slice(Courses.columns)
                .select { StudentCourses.studentId eq studentId }
                .map { CourseEntity.wrapRow(it) }
        }
    }

    override suspend fun getStudentsByCourseId(courseId: UUID): List<StudentEntity> {
        return transaction {
            (Students innerJoin StudentCourses)
                .slice(Students.columns)
                .select { StudentCourses.courseId eq courseId }
                .map { StudentEntity.wrapRow(it) }
        }
    }
}
