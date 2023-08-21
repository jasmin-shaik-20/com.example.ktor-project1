package com.example.routes

import com.example.dao.Person
import com.example.endpoints.ApiEndPoint
import com.example.endpoints.ApiEndPoint.TIME
import com.example.interfaceimpl.PersonInterfaceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import redis.clients.jedis.Jedis

fun Application.configurePersonRoutes(){
    val jedis = Jedis("localhost")
    jedis.connect()
    routing{
        route(ApiEndPoint.PERSON){
            val personInterfaceImpl : PersonInterfaceImpl by inject()
            post("/post details") {
                val post = call.receive<Person>()
                val person = personInterfaceImpl.createPersonData(post.id, post.name)
                if (person != null) {
                    call.respond(HttpStatusCode.OK,person)
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
                    jedis.setex("my_cached_data", TIME, dataFromSource)
                    call.respond(HttpStatusCode.OK, "Data from source: $dataFromSource")
                }
            }
        }
    }
}
