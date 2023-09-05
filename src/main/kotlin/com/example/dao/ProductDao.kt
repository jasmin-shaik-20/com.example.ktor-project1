package com.example.dao

import com.example.entities.ProductEntity
import java.util.UUID

interface ProductDao {
    suspend fun createProduct(userId: UUID, name: String, price: Int): ProductEntity
    fun getProductById(productId: UUID): ProductEntity?
    fun getProductByUserID(userId: UUID): List<ProductEntity>
    fun getAllProducts(): List<ProductEntity>
    fun updateProduct(productId: UUID, name: String, price: Int): Boolean
    fun deleteProduct(productId: UUID): Boolean
}
