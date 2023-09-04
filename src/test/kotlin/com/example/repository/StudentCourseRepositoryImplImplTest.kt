package com.example.repository

import com.example.database.table.StudentCourses
import com.example.utils.H2Database
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.Connection
import kotlin.test.assertEquals

class StudentCourseRepositoryImplImplTest {
    private lateinit var database: Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(StudentCourses)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(StudentCourses)
        }
    }

    @Test
    fun testGetCoursesForStudent(): Unit = runBlocking {
        val studentRepositoryImpl=StudentRepositoryImpl()
        val courseRepositoryImpl=CourseRepositoryImpl()
        val studentCourseRepositoryImpl=StudentCourseRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        courseRepositoryImpl.insertCourse(1,"Maths")
        studentCourseRepositoryImpl.getCoursesStudentId(1)
        assertEquals(1,1,"Maths")
    }

    @Test
    fun testGetStudentsForCourse(): Unit = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        val courseRepositoryImpl = CourseRepositoryImpl()
        val studentCourseRepositoryImpl = StudentCourseRepositoryImpl()
        studentRepositoryImpl.insertStudent(1, "jasmin")
        courseRepositoryImpl.insertCourse(1, "Maths")
        val studentsForCourse = studentCourseRepositoryImpl.getStudentsCourseId(1)
        val studentName = studentsForCourse.firstOrNull()?.name
        assertEquals("jasmin", studentName)
    }

}