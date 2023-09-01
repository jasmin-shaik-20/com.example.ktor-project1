package com.example.repository

import com.example.dao.Products
import com.example.utils.H2Database
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import java.sql.Connection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProductRepositoryTest {
    private lateinit var database: Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Products)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Products)
        }
    }

    //sucess
    @Test
    fun testInsertProductSuccess() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val productRepository = ProductRepository()
        val newProduct = productRepository.insertProduct(1, 1, "Product1", 100)
        assertEquals(1, newProduct?.userId)
        assertEquals("Product1", newProduct?.name)
        assertEquals(100, newProduct?.price)
    }


    @Test
    fun testGetAllProducts() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val productRepository = ProductRepository()
        productRepository.insertProduct(1, 1, "Product1", 100)
        productRepository.insertProduct(2, 1, "Product2", 200)
        val allProducts = productRepository.getAllProducts()
        assertEquals(2, allProducts.size)
    }

    @Test
    fun testGetProductsById() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val productRepository = ProductRepository()
        productRepository.insertProduct(1, 1, "Product1", 100)
        productRepository.insertProduct(2, 1, "Product2", 200)
        val userProducts = productRepository.getProductsById(1)
        assertEquals(2, userProducts.size)
    }

    @Test
    fun testGetProductSuccess() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val productRepository = ProductRepository()
        productRepository.insertProduct(1, 1, "Product1", 100)
        val product = productRepository.getProduct(1)
        assertNotNull(product)
        assertEquals("Product1", product?.name)
        assertEquals(100, product?.price)
    }

    @Test
    fun testDeleteProductSuccess() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val productRepository = ProductRepository()
        productRepository.insertProduct(1, 1, "Product1", 100)
        val deleteResult = productRepository.deleteProduct(1)
        assertTrue(deleteResult)
        val product = productRepository.getProduct(1)
        assertNull(product)
    }
    @Test
    fun testEditProductSuccess() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val productRepository = ProductRepository()
        productRepository.insertProduct(1, 1, "Product1", 100)
        val editResult = productRepository.editProduct(1, "NewProduct", 200)
        assertTrue(editResult)
        val updatedProduct = productRepository.getProduct(1)
        assertEquals("NewProduct", updatedProduct?.name)
        assertEquals(200, updatedProduct?.price)
    }

    //failure
    @Test
    fun testGetProductNotFound() = runBlocking {
        val productRepository = ProductRepository()
        val product = productRepository.getProduct(1)
        assertNull(product)
    }

    @Test
    fun testEditProductNotFound() = runBlocking {
        val productRepository = ProductRepository()
        val editResult = productRepository.editProduct(1, "NewProduct", 200)
        assertFalse(editResult)
    }

    @Test
    fun testDeleteProductNotFound() = runBlocking {
        val productRepository = ProductRepository()
        val deleteResult = productRepository.deleteProduct(1)
        assertFalse(deleteResult)
    }

}
