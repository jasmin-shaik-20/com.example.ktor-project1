package com.example.interfaceimpl

import com.example.dao.Course
import com.example.dao.Courses
import com.example.dao.StudentCourses
import com.example.interfaces.CourseInterface
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CourseInterfaceImpl : CourseInterface {
    override suspend fun insertCourse(studentId: Int, name: String): Course? = dbQuery {
        val insert = Courses.insert {
            it[Courses.studentId] = studentId
            it[Courses.name] = name
        }
        val courseId=insert.resultedValues?.singleOrNull()?.get(Courses.id)

        if(courseId!=null) {
            StudentCourses.insert {
                it[StudentCourses.studentId] = studentId
                it[StudentCourses.courseId] = courseId
            }
        }

        courseId?.let{ courseId ->
            val result = Courses.select { Courses.id eq courseId }.singleOrNull()
            result?.let(::rowToCourse)
        }
    }
    override suspend fun getAllCourses(): List<Course> = dbQuery {
        Courses.selectAll().map(::rowToCourse)
    }

    override suspend fun deleteCourse(id: Int): Boolean = dbQuery {
        val delCourse=Courses.deleteWhere { Courses.id eq id }
        delCourse>0
    }

    override suspend fun editCourse(id: Int, newName: String): Boolean = dbQuery{
        val editCourse=Courses.update({Courses.id eq id}){
            it[name]=newName
        }
        editCourse>0
    }
    override suspend fun getCourseById(id: Int): Course? = dbQuery {
        Courses.select { Courses.id eq id }.map(::rowToCourse).singleOrNull()
    }

}

private fun rowToCourse(row: ResultRow) =
    Course(row[Courses.id], row[Courses.studentId], row[Courses.name])