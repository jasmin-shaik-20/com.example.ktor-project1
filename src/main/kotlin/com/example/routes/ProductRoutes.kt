package com.example.routes

import com.example.dao.Product
import com.example.dao.Products
import com.example.interfaces.ProductInterfaceImpl
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
        route("/product"){
            val productInterfaceImpl= ProductInterfaceImpl()
            get{
                val getProducts= transaction {
                    Products.selectAll().
                    map { mapOf("productid" to it[Products.productid],
                        "userid" to it[Products.userid],
                        "name" to it[Products.name],
                        "price" to it[Products.price]) }
                }
                if(getProducts!=null) {
                    call.respond(getProducts)
                }
                else{
                    throw Throwable()
                }
            }

            post{
                val insert=call.receive<Product>()
                val postProduct=productInterfaceImpl
                    .insertProduct(insert.productid,insert.userid,insert.name,insert.price)
                if(postProduct!=null) {
                    call.respond(HttpStatusCode.Created, postProduct)
                }
            }

            get("/{id?}"){
                val id= call.parameters["id"]?:return@get throw InvalidIDException()
                val getid=productInterfaceImpl.getProduct(id.toInt())
                if(getid!=null){
                    call.respond(getid)
                }
                else{
                    throw ProductNotFoundException()
                }
            }


        }
    }
}