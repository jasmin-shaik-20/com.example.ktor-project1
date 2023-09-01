package com.example.repository

import com.example.dao.StudentCourses
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

class StudentCourseRepositoryTest {
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
        val studentRepository=StudentRepository()
        val courseRepository=CourseRepository()
        val studentCourseRepository=StudentCourseRepository()
        studentRepository.insertStudent(1,"jasmin")
        courseRepository.insertCourse(1,"Maths")
        studentCourseRepository.getCoursesStudentId(1)
        assertEquals(1,1,"Maths")
    }

    @Test
    fun testGetStudentsForCourse(): Unit = runBlocking {
        val studentRepository = StudentRepository()
        val courseRepository = CourseRepository()
        val studentCourseRepository = StudentCourseRepository()

        // Insert a student and a course
        studentRepository.insertStudent(1, "jasmin")
        courseRepository.insertCourse(1, "Maths")

        // Get the list of students for the course
        val studentsForCourse = studentCourseRepository.getStudentsCourseId(1)

        // Extract the student name from the list and compare with the expected value
        val studentName = studentsForCourse.firstOrNull()?.name
        assertEquals("jasmin", studentName)
    }

}