package com.example.repository

import com.example.database.table.Products
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

class ProductRepositoryImplTest {
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
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val productRepositoryImpl = ProductRepositoryImpl()
        val newProduct = productRepositoryImpl.insertProduct(1, 1, "Product1", 100)
        assertEquals(1, newProduct?.userId)
        assertEquals("Product1", newProduct?.name)
        assertEquals(100, newProduct?.price)
    }


    @Test
    fun testGetAllProducts() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val productRepositoryImpl = ProductRepositoryImpl()
        productRepositoryImpl.insertProduct(1, 1, "Product1", 100)
        productRepositoryImpl.insertProduct(2, 1, "Product2", 200)
        val allProducts = productRepositoryImpl.getAllProducts()
        assertEquals(2, allProducts.size)
    }

    @Test
    fun testGetProductsById() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val productRepositoryImpl = ProductRepositoryImpl()
        productRepositoryImpl.insertProduct(1, 1, "Product1", 100)
        productRepositoryImpl.insertProduct(2, 1, "Product2", 200)
        val userProducts = productRepositoryImpl.getProductsById(1)
        assertEquals(2, userProducts.size)
    }

    @Test
    fun testGetProductSuccess() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val productRepositoryImpl = ProductRepositoryImpl()
        productRepositoryImpl.insertProduct(1, 1, "Product1", 100)
        val product = productRepositoryImpl.getProduct(1)
        assertNotNull(product)
        assertEquals("Product1", product?.name)
        assertEquals(100, product?.price)
    }

    @Test
    fun testDeleteProductSuccess() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val productRepositoryImpl = ProductRepositoryImpl()
        productRepositoryImpl.insertProduct(1, 1, "Product1", 100)
        val deleteResult = productRepositoryImpl.deleteProduct(1)
        assertTrue(deleteResult)
        val product = productRepositoryImpl.getProduct(1)
        assertNull(product)
    }
    @Test
    fun testEditProductSuccess() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val productRepositoryImpl = ProductRepositoryImpl()
        productRepositoryImpl.insertProduct(1, 1, "Product1", 100)
        val editResult = productRepositoryImpl.editProduct(1, "NewProduct", 200)
        assertTrue(editResult)
        val updatedProduct = productRepositoryImpl.getProduct(1)
        assertEquals("NewProduct", updatedProduct?.name)
        assertEquals(200, updatedProduct?.price)
    }

    //failure
    @Test
    fun testGetProductNotFound() = runBlocking {
        val productRepositoryImpl = ProductRepositoryImpl()
        val product = productRepositoryImpl.getProduct(1)
        assertNull(product)
    }

    @Test
    fun testEditProductNotFound() = runBlocking {
        val productRepositoryImpl = ProductRepositoryImpl()
        val editResult = productRepositoryImpl.editProduct(1, "NewProduct", 200)
        assertFalse(editResult)
    }

    @Test
    fun testDeleteProductNotFound() = runBlocking {
        val productRepositoryImpl = ProductRepositoryImpl()
        val deleteResult = productRepositoryImpl.deleteProduct(1)
        assertFalse(deleteResult)
    }

}
