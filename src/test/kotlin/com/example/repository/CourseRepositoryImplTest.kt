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

class CourseRepositoryImplTest {
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
        val courseRepositoryImpl = CourseRepositoryImpl()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        val newCourse = courseRepositoryImpl.insertCourse(1, "Math")
        assertEquals(1, newCourse?.id)
        assertEquals(1, newCourse?.studentId)
        assertEquals("Math", newCourse?.name)
    }

    @Test
    fun testGetAllCourses() = runBlocking {
        val courseRepositoryImpl = CourseRepositoryImpl()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        courseRepositoryImpl.insertCourse(1, "Math")
        courseRepositoryImpl.insertCourse(2, "Science")
        courseRepositoryImpl.insertCourse(1, "History")
        val allCourses = courseRepositoryImpl.getAllCourses()
        assertEquals(3, allCourses.size)

    }
    @Test
    fun testDeleteCourseSuccess() = runBlocking {
        val courseRepositoryImpl = CourseRepositoryImpl()
        val studentRepositoryImpl = StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1, "Jasmin")
        val course = courseRepositoryImpl.insertCourse(1, "Math")
        val deleteResult = courseRepositoryImpl.deleteCourse(course!!.id)
        assertTrue(deleteResult)
        val deletedCourse = courseRepositoryImpl.getCourseById(course.id)
        assertNull(deletedCourse)
    }
    @Test
    fun testEditCourseSuccess() = runBlocking {
        val courseRepositoryImpl = CourseRepositoryImpl()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        courseRepositoryImpl.insertCourse(1, "Math")
        val editResult = courseRepositoryImpl.editCourse(1, "Science")
        assertTrue(editResult)
        val updatedCourse = courseRepositoryImpl.getCourseById(1)
        assertEquals("Science", updatedCourse?.name)
    }
    @Test
    fun testGetCourseByIdSuccess() = runBlocking {
        val courseRepositoryImpl = CourseRepositoryImpl()
        val studentRepositoryImpl=StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1,"jasmin")
        courseRepositoryImpl.insertCourse(1, "Math")
        val retrievedCourse = courseRepositoryImpl.getCourseById(1)
        assertEquals("Math", retrievedCourse?.name)
    }

    //failure

    @Test
    fun testDeleteCourseNotFound() = runBlocking {
        val courseRepositoryImpl = CourseRepositoryImpl()
        val deleteResult = courseRepositoryImpl.deleteCourse(1)
        assertFalse(deleteResult)
    }

    @Test
    fun testEditCourseNotFound() = runBlocking {
        val courseRepositoryImpl = CourseRepositoryImpl()
        val editResult = courseRepositoryImpl.editCourse(1, "Science")
        assertFalse(editResult)
    }

    @Test
    fun testGetCourseByIdNotFound() = runBlocking {
        val courseRepositoryImpl = CourseRepositoryImpl()
        val retrievedCourse = courseRepositoryImpl.getCourseById(1)
        assertNull(retrievedCourse)
    }
}
