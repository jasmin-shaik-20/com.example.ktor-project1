package com.example.interfaces

import com.example.dao.Product
import com.example.dao.Products
import com.example.plugins.ProductInterface
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class ProductInterfaceImpl : ProductInterface {
    override suspend fun insertProduct(productid: Int, userid: Int, name: String, price: Int): Product? = dbQuery{
        val insert=Products.insert {
            it[Products.userid]=userid
            it[Products.name]=name
            it[Products.price]=price
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToProduct)
    }


    override suspend fun getProduct(id: Int): Product? = dbQuery{
        Products.select(Products.productid eq id).map(::rowToProduct).singleOrNull()
    }
}


private fun rowToProduct(row:ResultRow)=
    Product(row[Products.productid],row[Products.userid],row[Products.name],row[Products.price])