package com.example.services

import com.example.database.table.Product
import com.example.plugins.ProductNotFoundException
import com.example.repository.ProductRepositoryImpl

class ProductServices {

    private val productRepositoryImpl = ProductRepositoryImpl()

    suspend fun handleGetProducts(): List<Product> {
        val getProducts = productRepositoryImpl.getAllProducts()
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
            val postProduct = productRepositoryImpl.insertProduct(
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
        val getProduct = productRepositoryImpl.getProductsById(userId)
        if (getProduct.isEmpty()) {
            throw ProductNotFoundException()
        } else {
            return getProduct
        }
    }

    suspend fun handleGetProductById(id: Int): Product {
        val fetchedProduct = productRepositoryImpl.getProduct(id) ?: throw ProductNotFoundException()
        return fetchedProduct
    }

    suspend fun handleDeleteProduct(id: Int): Boolean {
        val deleted = productRepositoryImpl.deleteProduct(id)
        if (deleted) {
            return true
        } else {
            throw ProductNotFoundException()
        }
    }

    suspend fun handleUpdateProduct(id:Int,productDetails: Product): Boolean {
        val editProduct = productRepositoryImpl.editProduct(id,
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
