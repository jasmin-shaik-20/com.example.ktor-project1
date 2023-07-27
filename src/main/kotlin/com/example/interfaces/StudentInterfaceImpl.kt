package com.example.interfaces

import com.example.dao.*
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class StudentInterfaceImpl : StudentInterface {
    override suspend fun insertStudent(id: Int, name: String): student? = dbQuery{
        val insert=students.insert {
            it[students.name]=name
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToStudent)
    }

    override suspend fun getAllStudents(){
       val get=transaction { students.selectAll().map {
           mapOf("id" to it[students.id], "name" to it[students.name])
       } }
    }

    override suspend fun getStudentById(id: Int): student? = dbQuery {
        students.select { students.id eq id }.map(::rowToStudent).singleOrNull()
    }
}


class CourseInterfaceImpl : CourseInterface{
    override suspend fun insertCourse(id: Int, student_id: Int, name: String): course? = dbQuery{
      val insert=courses.insert {
          it[courses.student_id]=student_id
          it[courses.name]=name
      }
        studentcourses.insert {
            it[studentcourses.studentid]=student_id
            it[studentcourses.courseid]=id
        }

        insert.resultedValues?.singleOrNull()?.let(::rowToCourse)
    }

    override suspend fun getAllCourses() {
        courses.selectAll().map { mapOf("id" to it[courses.id],"student_id" to it[courses.student_id],"name" to it[courses.name]) }
    }

    override suspend fun getCourseById(id: Int): course? = dbQuery{
        courses.select { courses.id eq id }.map(::rowToCourse).singleOrNull()
    }

}

private fun rowToStudent(row: ResultRow)=
    student(row[students.id],row[students.name])


private fun rowToCourse(row: ResultRow)=
    course(row[courses.id],row[courses.student_id],row[courses.name])