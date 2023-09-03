package com.example.services

import com.example.dao.*
import com.example.plugins.ProductNotFoundException
import com.example.repository.ProductRepository
import com.example.repository.UsersRepository
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
import kotlin.test.assertFailsWith

class ProductServiceTest {

    private val productRepository = ProductRepository()
    private val productServices = ProductServices()
    private val usersRepository = UsersRepository()
    private lateinit var database: Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Users, Products)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Users, Products)
        }
    }

    //sucess
    @Test
    fun testHandleGetAllProducts() {
        runBlocking {
            val user1 = User(1, "jasmin")
            usersRepository.createUser(user1.id, user1.name)
            val product1 = Product(1, user1.id, "cake", 40)
            val product2 = Product(2, user1.id, "chocolate", 50)
            productRepository.insertProduct(product1.productId, product1.userId, product1.name, product1.price)
            productRepository.insertProduct(product2.productId, product2.userId, product2.name, product2.price)
            val getProducts = productServices.handleGetProducts()
            assertEquals(listOf(product1, product2), getProducts)
        }
    }

    @Test
    fun testHandlePostProduct() {
        runBlocking {
            val user1 = User(1, "jasmin")
            usersRepository.createUser(user1.id, user1.name)
            val product = Product(1, user1.id, "apples", 100)
            val details = productServices.handlePostProduct(product, 4, 10)
            assertEquals(product, details)
        }
    }

    @Test
    fun testHandleGetProductById() {
        runBlocking {
            val user = User(1, "jasmin")
            usersRepository.createUser(user.id, user.name)
            val product = Product(1, user.id, "cake", 40)
            productRepository.insertProduct(product.productId, product.userId, product.name, product.price)
            val retrievedProduct = productServices.handleGetProductById(product.productId)
            assertEquals(product, retrievedProduct)
        }
    }

    @Test
    fun testHandleGetProductByUserId(){
        runBlocking {
            val user = User(1, "jasmin")
            usersRepository.createUser(user.id, user.name)
            val product1 = Product(1, user.id, "cake", 40)
            val product2 = Product(2, user.id, "chocolate", 40)
            productRepository.insertProduct(product1.productId, product1.userId, product1.name, product1.price)
            productRepository.insertProduct(product2.productId, product2.userId, product2.name, product2.price)
            val products=productServices.handleGetProductsByUserId(user.id)
            assertEquals(listOf(product1,product2),products)

        }
    }

    @Test
    fun testHandleDeleteProduct(){
        runBlocking {
            val user = User(1, "jasmin")
            usersRepository.createUser(user.id, user.name)
            val product = Product(1, user.id, "cake", 40)
            productRepository.insertProduct(product.productId, product.userId, product.name, product.price)
            val deleteProduct = productServices.handleDeleteProduct(product.productId)
            assertEquals(true, deleteProduct)
        }
    }

    @Test
    fun testHandleEditProduct(){
        runBlocking {
            val user = User(1, "jasmin")
            usersRepository.createUser(user.id, user.name)
            val product = Product(1, user.id, "cake", 40)
            productRepository.insertProduct(product.productId, product.userId, product.name, product.price)
            val updatedProduct=Product(product.productId,product.userId,"chocolate cake",100)
            val editDetails=productServices.handleUpdateProduct(updatedProduct.productId,updatedProduct)
            assertEquals(true,editDetails)
        }
    }

    //failure
    @Test
    fun testHandlePostProductInvalidName(){
        runBlocking {
            val product=Product(1,1,"ice cream",120)
            assertFailsWith<Exception>("Invalid length"){
                productServices.handlePostProduct(product,4,7)
            }
        }
    }

    @Test
    fun testHandleGetProductNotFound(){
        runBlocking {
            val productId=2
            assertFailsWith<ProductNotFoundException>("Product not found"){
                productServices.handleGetProductById(productId)
            }
        }
    }

    @Test
    fun testHandleDeleteProductNotFound(){
        runBlocking {
            val productId=2
            assertFailsWith<ProductNotFoundException>("Product not found"){
                productServices.handleDeleteProduct(productId)
            }
        }
    }

    @Test
    fun testHandleEditProductNotFound(){
        runBlocking {
            val productId=2
            val product=Product(2,2,"cake",100)
            assertFailsWith<ProductNotFoundException>("Product not found"){
                productServices.handleUpdateProduct(productId,product)
            }
        }
    }
}