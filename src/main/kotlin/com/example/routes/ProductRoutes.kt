package com.example.routes

import com.example.dao.Product
import com.example.dao.Products
import com.example.dao.User
import com.example.file.ApiEndPoint
import com.example.interfaceimpl.ProductInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.ProductNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureProductRoutes(){
    routing{
        route(ApiEndPoint.PRODUCT){
            val productInterfaceImpl= ProductInterfaceImpl()
            get{
                val getProducts= productInterfaceImpl.getAllProducts()
                if(getProducts.isEmpty()) {
                    throw ProductNotFoundException()
                }
                else{
                    call.respond(getProducts)
                }
            }

            post{
                val insert=call.receive<Product>()
                val postProduct=productInterfaceImpl
                    .insertProduct(insert.productId,insert.userId,insert.name,insert.price)
                if(postProduct!=null) {
                    call.respond(HttpStatusCode.Created, postProduct)
                }
                else{
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            get("/userId/{id?}"){
                val id= call.parameters["id"]?:throw  InvalidIDException()
                val getProduct=productInterfaceImpl.getProductsById(id.toInt())
                if(getProduct.isEmpty()){
                    call.respond("No products found with given userId $id")
                }
                else
                {
                    call.respond(getProduct)
                }
            }

            get("/{id?}"){
                val id= call.parameters["id"]?:return@get throw InvalidIDException()
                val fetid=productInterfaceImpl.getProduct(id.toInt())
                if(fetid!=null){
                    call.respond(fetid)
                }
                else{
                    throw ProductNotFoundException()
                }
            }

            delete("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val delProduct=productInterfaceImpl.deleteProduct(id)
                    if(delProduct){
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw ProductNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val product=call.receive<Product>()
                    val editProduct=productInterfaceImpl.editProduct(product.productId,product.name,product.price)
                    if(editProduct){
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw ProductNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }


        }
    }
}