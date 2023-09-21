package com.example.services

import com.example.repository.ProductRepositoryImpl
import com.example.entities.ProductEntity
import com.example.exceptions.ProductNameInvalidLengthException
import com.example.exceptions.ProductNotFoundException
import com.example.model.Product
import com.example.model.ProductInput
import com.example.repository.ProfileRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ProductServices:KoinComponent {

    private val productRepositoryImpl by inject<ProductRepositoryImpl>()

    suspend fun handleGetProducts(): List<Product> {
        return productRepositoryImpl.getAllProducts()
    }

    suspend fun handlePostProduct(
        productInput: ProductInput,
        productNameMinLength: Int?,
        productNameMaxLength: Int?
    ): Product {
        if (productInput.name.length in productNameMinLength!!..productNameMaxLength!!) {
            return productRepositoryImpl.createProduct(productInput)
        } else {
            throw ProductNameInvalidLengthException()
        }
    }

    suspend fun handleGetProductsByUserId(userId: UUID): List<Product> {
        val getProduct = productRepositoryImpl.getProductByUserID(userId)
        if (getProduct.isEmpty()) {
            throw ProductNotFoundException()
        } else {
            return getProduct
        }
    }

    suspend fun handleGetProductById(productId: UUID): Product {
        return productRepositoryImpl.getProductById(productId) ?: throw ProductNotFoundException()
    }

    suspend fun handleDeleteProduct(productId: UUID): Boolean {
        val deleted = productRepositoryImpl.deleteProduct(productId)
        if (!deleted) {
            throw ProductNotFoundException()
        }
        return true
    }

    suspend fun handleUpdateProduct(productId:UUID, productDetails: Product): Boolean {
        val editProduct = productRepositoryImpl.updateProduct(productId,
            productDetails.name,
            productDetails.price
        )
        if (!editProduct) {
            throw ProductNotFoundException()
        }
        return true
    }
}
