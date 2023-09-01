package com.example.repository

import com.example.dao.Students
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

class StudentRepositoryTest {
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
        val studentRepository = StudentRepository()
        val newStudent = studentRepository.insertStudent(1, "John Doe")
        assertEquals(1, newStudent?.id)
        assertEquals("John Doe", newStudent?.name)
    }

    @Test
    fun testGetAllStudents() = runBlocking {
        val studentRepository = StudentRepository()
        studentRepository.insertStudent(1, "John Doe")
        studentRepository.insertStudent(2, "Jane Smith")
        studentRepository.insertStudent(3, "Michael Johnson")
        val allStudents = studentRepository.getAllStudents()
        assertEquals(3, allStudents.size)
    }

    @Test
    fun testDeleteStudentSuccess() = runBlocking {
        val studentRepository = StudentRepository()
        studentRepository.insertStudent(1, "John Doe")
        val deleteResult = studentRepository.deleteStudent(1)
        assertTrue(deleteResult)
        val deletedStudent = studentRepository.getStudentById(1)
        assertNull(deletedStudent)
    }

    @Test
    fun testEditStudentSuccess() = runBlocking {
        val studentRepository = StudentRepository()
        studentRepository.insertStudent(1, "John Doe")
        val editResult = studentRepository.editStudent(1, "Jane Smith")
        assertTrue(editResult)
        val updatedStudent = studentRepository.getStudentById(1)
        assertEquals("Jane Smith", updatedStudent?.name)
    }

    @Test
    fun testGetStudentByIdSuccess() = runBlocking {
        val studentRepository = StudentRepository()
        studentRepository.insertStudent(1, "John Doe")
        val retrievedStudent = studentRepository.getStudentById(1)
        assertEquals("John Doe", retrievedStudent?.name)
    }

    //failure

    @Test
    fun testDeleteStudentNotFound() = runBlocking {
        val studentRepository = StudentRepository()
        val deleteResult = studentRepository.deleteStudent(1)
        assertFalse(deleteResult)
    }

    @Test
    fun testEditStudentNotFound() = runBlocking {
        val studentRepository = StudentRepository()
        val editResult = studentRepository.editStudent(1, "Jane Smith")
        assertFalse(editResult)
    }

    @Test
    fun testGetStudentByIdNotFound() = runBlocking {
        val studentRepository = StudentRepository()
        val retrievedStudent = studentRepository.getStudentById(1)
        assertNull(retrievedStudent)
    }
}