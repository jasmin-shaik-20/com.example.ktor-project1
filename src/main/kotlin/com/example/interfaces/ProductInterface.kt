package com.example.plugins

import com.example.dao.Product

interface ProductInterface {
    suspend fun insertProduct(productId:Int,userId:Int,name:String,price:Int): Product?
    suspend fun getProduct(id:Int):Product?
    suspend fun getProductsById(userId: Int):List<Product>
    suspend fun getAllProducts():List<Product>
    suspend fun deleteProduct(productId: Int):Boolean
    suspend fun editProduct(productId: Int,newName:String,newPrice:Int):Boolean
}
