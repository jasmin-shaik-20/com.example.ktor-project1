package com.example.repository

import com.example.database.table.Students
import com.example.utils.H2Database
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import java.sql.Connection
import kotlin.test.Test

class StudentRepositoryImplTest {
    private lateinit var database: Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Students)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Students)
        }
    }

    //sucess
    @Test
    fun testInsertStudentSuccess() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        val newStudent = studentRepositoryImpl.insertStudent(1, "John Doe")
        assertEquals(1, newStudent?.id)
        assertEquals("John Doe", newStudent?.name)
    }

    @Test
    fun testGetAllStudents() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1, "John Doe")
        studentRepositoryImpl.insertStudent(2, "Jane Smith")
        studentRepositoryImpl.insertStudent(3, "Michael Johnson")
        val allStudents = studentRepositoryImpl.getAllStudents()
        assertEquals(3, allStudents.size)
    }

    @Test
    fun testDeleteStudentSuccess() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1, "John Doe")
        val deleteResult = studentRepositoryImpl.deleteStudent(1)
        assertTrue(deleteResult)
        val deletedStudent = studentRepositoryImpl.getStudentById(1)
        assertNull(deletedStudent)
    }

    @Test
    fun testEditStudentSuccess() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1, "John Doe")
        val editResult = studentRepositoryImpl.editStudent(1, "Jane Smith")
        assertTrue(editResult)
        val updatedStudent = studentRepositoryImpl.getStudentById(1)
        assertEquals("Jane Smith", updatedStudent?.name)
    }

    @Test
    fun testGetStudentByIdSuccess() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        studentRepositoryImpl.insertStudent(1, "John Doe")
        val retrievedStudent = studentRepositoryImpl.getStudentById(1)
        assertEquals("John Doe", retrievedStudent?.name)
    }

    //failure

    @Test
    fun testDeleteStudentNotFound() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        val deleteResult = studentRepositoryImpl.deleteStudent(1)
        assertFalse(deleteResult)
    }

    @Test
    fun testEditStudentNotFound() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        val editResult = studentRepositoryImpl.editStudent(1, "Jane Smith")
        assertFalse(editResult)
    }

    @Test
    fun testGetStudentByIdNotFound() = runBlocking {
        val studentRepositoryImpl = StudentRepositoryImpl()
        val retrievedStudent = studentRepositoryImpl.getStudentById(1)
        assertNull(retrievedStudent)
    }
}