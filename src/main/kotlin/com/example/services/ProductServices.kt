package com.example.services

import com.example.repository.ProductRepositoryImpl
import com.example.entities.ProductEntity
import com.example.exceptions.ProductNameInvalidLengthException
import com.example.exceptions.ProductNotFoundException
import com.example.repository.ProfileRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ProductServices:KoinComponent {

    private val productRepositoryImpl by inject<ProductRepositoryImpl>()

    fun handleGetProducts(): List<ProductEntity> {
        return productRepositoryImpl.getAllProducts()
    }

    suspend fun handlePostProduct(
        productDetails: ProductEntity,
        productNameMinLength: Int?,
        productNameMaxLength: Int?
    ): ProductEntity {
        if (productDetails.name.length in productNameMinLength!!..productNameMaxLength!!) {
            return productRepositoryImpl.createProduct(
                productDetails.userId.id.value,
                productDetails.name,
                productDetails.price
            )
        } else {
            throw ProductNameInvalidLengthException()
        }
    }

    fun handleGetProductsByUserId(userId: UUID): List<ProductEntity> {
        val getProduct = productRepositoryImpl.getProductByUserID(userId)
        if (getProduct.isEmpty()) {
            throw ProductNotFoundException()
        } else {
            return getProduct
        }
    }

    fun handleGetProductById(id: UUID): ProductEntity {
        return productRepositoryImpl.getProductById(id) ?: throw ProductNotFoundException()
    }

    fun handleDeleteProduct(id: UUID): Boolean {
        val deleted = productRepositoryImpl.deleteProduct(id)
        if (!deleted) {
            throw ProductNotFoundException()
        }
        return true
    }

    fun handleUpdateProduct(id:UUID,productDetails: ProductEntity): Boolean {
        val editProduct = productRepositoryImpl.updateProduct(id,
            productDetails.name,
            productDetails.price
        )
        if (!editProduct) {
            throw ProductNotFoundException()
        }
        return true
    }
}
