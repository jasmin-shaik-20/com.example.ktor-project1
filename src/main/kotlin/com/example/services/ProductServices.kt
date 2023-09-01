package com.example.services

import com.example.dao.Product
import com.example.plugins.InvalidIDException
import com.example.plugins.ProductNotFoundException
import com.example.repository.ProductRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ProductServices {

    private val productRepository = ProductRepository()

    suspend fun handleGetProducts(): List<Product> {
        val getProducts = productRepository.getAllProducts()
        return if (getProducts.isEmpty()) {
            emptyList()
        } else {
            getProducts
        }
    }

    suspend fun handlePostProduct(
        productDetails: Product,
        productNameMinLength: Int?,
        productNameMaxLength: Int?
    ): Product {
        if (productDetails.name.length in productNameMinLength!!..productNameMaxLength!!) {
            val postProduct = productRepository.insertProduct(
                productDetails.productId,
                productDetails.userId,
                productDetails.name,
                productDetails.price
            )
            return postProduct ?: throw Exception("Product creation failed")
        } else {
            throw Exception("Invalid name length")
        }
    }

    suspend fun handleGetProductsByUserId(userId: Int): List<Product> {
        val getProduct = productRepository.getProductsById(userId)
        if (getProduct.isEmpty()) {
            throw ProductNotFoundException()
        } else {
            return getProduct
        }
    }

    suspend fun handleGetProductById(id: Int): Product {
        val fetchedProduct = productRepository.getProduct(id) ?: throw ProductNotFoundException()
        return fetchedProduct
    }

    suspend fun handleDeleteProduct(id: Int): Boolean {
        val deleted = productRepository.deleteProduct(id)
        if (deleted) {
            return true
        } else {
            throw ProductNotFoundException()
        }
    }

    suspend fun handleUpdateProduct(id:Int,productDetails: Product): Boolean {
        val editProduct = productRepository.editProduct(id,
            productDetails.name,
            productDetails.price
        )
        if (editProduct) {
            return true
        } else {
            throw ProductNotFoundException()
        }
    }
}
