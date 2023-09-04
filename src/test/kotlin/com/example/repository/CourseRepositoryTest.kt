package com.example.repository

import com.example.database.table.Courses
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
            SchemaUtils.create(Courses, StudentCourses)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Courses,StudentCourses)
        }
    }

    //sucess
    @Test
    fun testInsertCourseSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        val newCourse = courseRepository.insertCourse(1, "Math")
        assertEquals(1, newCourse?.id)
        assertEquals(1, newCourse?.studentId)
        assertEquals("Math", newCourse?.name)
    }

    @Test
    fun testGetAllCourses() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        courseRepository.insertCourse(1, "Math")
        courseRepository.insertCourse(2, "Science")
        courseRepository.insertCourse(1, "History")
        val allCourses = courseRepository.getAllCourses()
        assertEquals(3, allCourses.size)

    }
    @Test
    fun testDeleteCourseSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepositoryImpl = StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1, "Jasmin")
        val course = courseRepository.insertCourse(1, "Math")
        val deleteResult = courseRepository.deleteCourse(course!!.id)
        assertTrue(deleteResult)
        val deletedCourse = courseRepository.getCourseById(course.id)
        assertNull(deletedCourse)
    }
    @Test
    fun testEditCourseSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        courseRepository.insertCourse(1, "Math")
        val editResult = courseRepository.editCourse(1, "Science")
        assertTrue(editResult)
        val updatedCourse = courseRepository.getCourseById(1)
        assertEquals("Science", updatedCourse?.name)
    }
    @Test
    fun testGetCourseByIdSuccess() = runBlocking {
        val courseRepository = CourseRepository()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        courseRepository.insertCourse(1, "Math")
        val retrievedCourse = courseRepository.getCourseById(1)
        assertEquals("Math", retrievedCourse?.name)
    }

    //failure

    @Test
    fun testDeleteCourseNotFound() = runBlocking {
        val courseRepository = CourseRepository()
        val deleteResult = courseRepository.deleteCourse(1)
        assertFalse(deleteResult)
    }

    @Test
    fun testEditCourseNotFound() = runBlocking {
        val courseRepository = CourseRepository()
        val editResult = courseRepository.editCourse(1, "Science")
        assertFalse(editResult)
    }

    @Test
    fun testGetCourseByIdNotFound() = runBlocking {
        val courseRepository = CourseRepository()
        val retrievedCourse = courseRepository.getCourseById(1)
        assertNull(retrievedCourse)
    }
}
