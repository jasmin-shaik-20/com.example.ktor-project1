package com.example.routes

import com.example.dao.Person
import com.example.file.ApiEndPoint
import com.example.interfaceimpl.PersonInterfaceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import redis.clients.jedis.Jedis

fun Application.configurePersonRoutes(){
    val jedis = Jedis("localhost")
    jedis.connect()
    routing{
        route(ApiEndPoint.PERSON){
            val personInterfaceImpl= PersonInterfaceImpl()
            post("/post details") {
                val post = call.receive<Person>()
                val person = personInterfaceImpl.createPersonData(post.id, post.name)
                if (person != null) {
                    call.respond(person)
                }
            }

            get("/data") {
                val cachedData = jedis.get("my_cached_data")
                if (cachedData != null) {
                    call.respond(HttpStatusCode.OK, "Cached data: $cachedData")
                } else {
                    val id = call.request.queryParameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid 'id' parameter")
                        return@get
                    }
                    val dataFromSource = personInterfaceImpl.fetchData(id)
                    jedis.setex("my_cached_data", 300, dataFromSource)
                    call.respond(HttpStatusCode.OK, "Data from source: $dataFromSource")
                }
            }
        }
    }
}