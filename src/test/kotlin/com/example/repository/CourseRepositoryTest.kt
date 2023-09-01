package com.example.repository

import com.example.dao.Courses
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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CourseRepositoryTest {
    private lateinit var database: Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Courses,StudentCourses)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Courses,StudentCourses)
        }
    }

    @Test
    fun testInsertCourseSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepository=StudentRepository()
        studentRepository.insertStudent(1,"jasmin")
        val newCourse = courseRepository.insertCourse(1, "Math")
        assertEquals(1, newCourse?.id)
        assertEquals(1, newCourse?.studentId)
        assertEquals("Math", newCourse?.name)
    }

    @Test
    fun testGetAllCourses() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepository=StudentRepository()
        studentRepository.insertStudent(1,"jasmin")

        // Insert some test courses
        courseRepository.insertCourse(1, "Math")
        courseRepository.insertCourse(2, "Science")
        courseRepository.insertCourse(1, "History")

        val allCourses = courseRepository.getAllCourses()

        assertEquals(3, allCourses.size)
        // You can add more assertions here to validate the courses in the list
    }
    @Test
    fun testDeleteCourseSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepository = StudentRepository()

        // Insert a test student using a coroutine
        studentRepository.insertStudent(1, "Jasmin")

        // Insert a test course using a coroutine
        val course = courseRepository.insertCourse(1, "Math")

        val deleteResult = courseRepository.deleteCourse(course!!.id)
        assertTrue(deleteResult)

        val deletedCourse = courseRepository.getCourseById(course.id)
        assertNull(deletedCourse)
    }


    @Test
    fun testDeleteCourseNotFound() = runBlocking {
        val courseRepository = CourseRepository()

        val deleteResult = courseRepository.deleteCourse(1)
        assertFalse(deleteResult)
    }

    @Test
    fun testEditCourseSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepository=StudentRepository()
        studentRepository.insertStudent(1,"jasmin")

        // Insert a test course
        courseRepository.insertCourse(1, "Math")

        val editResult = courseRepository.editCourse(1, "Science")
        assertTrue(editResult)

        val updatedCourse = courseRepository.getCourseById(1)
        assertEquals("Science", updatedCourse?.name)
    }

    @Test
    fun testEditCourseNotFound() = runBlocking {
        val courseRepository = CourseRepository()

        val editResult = courseRepository.editCourse(1, "Science")
        assertFalse(editResult)
    }

    @Test
    fun testGetCourseByIdSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepository=StudentRepository()
        studentRepository.insertStudent(1,"jasmin")

        // Insert a test course
        courseRepository.insertCourse(1, "Math")

        val retrievedCourse = courseRepository.getCourseById(1)
        assertEquals("Math", retrievedCourse?.name)
    }

    @Test
    fun testGetCourseByIdNotFound() = runBlocking {
        val courseRepository = CourseRepository()

        val retrievedCourse = courseRepository.getCourseById(1)
        assertNull(retrievedCourse)
    }
}
