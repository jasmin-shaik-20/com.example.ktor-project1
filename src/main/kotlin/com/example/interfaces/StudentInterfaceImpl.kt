package com.example.interfaces

import com.example.dao.*
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class StudentInterfaceImpl : StudentInterface {
    override suspend fun insertStudent(id: Int, name: String): student? = dbQuery {
        val insert = students.insert {
            it[students.name] = name
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToStudent)
    }

    override suspend fun getAllStudents():List<student> = dbQuery{
        students.selectAll().map(::rowToStudent)
    }

    override suspend fun getStudentById(id: Int): student? = dbQuery {
        students.select { students.id eq id }.map(::rowToStudent).singleOrNull()
    }
}


class CourseInterfaceImpl : CourseInterface {
    override suspend fun insertCourse(id: Int, student_id: Int, name: String): course? = dbQuery {
        val insert = courses.insert {
            it[courses.student_id] = student_id
            it[courses.name] = name
        }
        studentcourses.insert {
            it[studentcourses.studentid] = student_id
            it[studentcourses.courseid] = id
        }

        insert.resultedValues?.singleOrNull()?.let(::rowToCourse)
    }

    override suspend fun getAllCourses(): List<course> = dbQuery {
        courses
            .selectAll()
            .map(::rowToCourse)
    }

    override suspend fun getCourseById(id: Int): course? = dbQuery {
        courses.select { courses.id eq id }.map(::rowToCourse).singleOrNull()
    }

}

class StudentCourseInterfaceImpl : StudentCourseInterface {

    override suspend fun getCoursesBystudentId(id: Int): List<course> = dbQuery{
        return@dbQuery transaction {
            (courses innerJoin studentcourses).
            select{studentcourses.studentid eq id}.
            map(::rowToCourse)
        }
    }

    override suspend fun getStudentsBycourseId(id: Int): List<student> = dbQuery {
        return@dbQuery transaction {
            (students innerJoin studentcourses).select {
                studentcourses.courseid eq id
            }.map(::rowToStudent)
        }
    }
}

private fun rowToStudent(row: ResultRow) =
    student(row[students.id], row[students.name])


private fun rowToCourse(row: ResultRow) =
    course(row[courses.id], row[courses.student_id], row[courses.name])