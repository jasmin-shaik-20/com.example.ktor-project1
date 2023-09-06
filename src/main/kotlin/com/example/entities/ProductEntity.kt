package com.example.entities

import com.example.database.table.Products
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class ProductEntity(productId: EntityID<UUID>) : UUIDEntity(productId) {
    companion object : UUIDEntityClass<ProductEntity>(Products)
    var userId by UserEntity referencedOn Products.userId
    var name by Products.name
    var price by Products.price
}
