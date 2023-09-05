package com.example.repository

import com.example.dao.ProductDao
import com.example.database.table.Products
import com.example.database.table.Users
import com.example.entities.ProductEntity
import com.example.entities.UserEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ProductRepositoryImpl(id: EntityID<UUID>) : UUIDEntity(id), ProductDao {
    companion object : UUIDEntityClass<ProductRepositoryImpl>(Products)
    override suspend fun createProduct(userId: UUID, name: String, price: Int): ProductEntity {
        return transaction {
            val newProduct = ProductEntity.new {
                this.userId = UserEntity[EntityID(userId, Users)]
                this.name = name
                this.price = price
            }
            newProduct
        }
    }

    override fun getProductById(productId: UUID): ProductEntity? {
        return transaction {
            ProductEntity.findById(productId)
        }
    }

    override fun getProductByUserID(userId: UUID): List<ProductEntity> {
        return transaction {
            ProductEntity.find { Products.userId eq userId }
                .toList()
        }
    }

    override fun getAllProducts(): List<ProductEntity> {
        return transaction {
            ProductEntity.all().toList()
        }
    }

    override fun updateProduct(productId: UUID, name: String, price: Int): Boolean {
        return transaction {
            val product = ProductEntity.findById(productId)
            if (product != null) {
                product.name = name
                product.price = price
                true
            } else {
                false
            }
        }
    }

    override fun deleteProduct(productId: UUID): Boolean {
        return transaction {
            val product = ProductEntity.findById(productId)
            if (product != null) {
                product.delete()
                true
            } else {
                false
            }
        }
    }
}
