package com.example.dao

import com.example.entities.ProductEntity
import com.example.model.Product
import com.example.model.ProductInput
import com.example.model.UserProfileInput
import java.util.UUID

interface ProductDao {
    suspend fun createProduct(productInput: ProductInput): Product
    suspend fun getProductById(productId: UUID): Product?
    suspend fun getProductByUserID(userId: UUID): List<Product>
    suspend fun getAllProducts(): List<Product>
    suspend fun updateProduct(productId: UUID, name: String, price: Int): Boolean
    suspend fun deleteProduct(productId: UUID): Boolean
}
