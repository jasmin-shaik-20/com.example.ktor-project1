package com.example.routes

import com.example.dao.Person
import com.example.endpoints.ApiEndPoint
import com.example.endpoints.ApiEndPoint.TIME
import com.example.repository.PersonInterfaceImpl
import com.example.services.PersonServices
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
            val personServices: PersonServices by inject()
            post("/post details") {
                personServices.handlePostPersonDetails(call,personInterfaceImpl)
            }

            get("/data") {
                personServices.handleGetDataFromCacheOrSource(call,personInterfaceImpl,jedis)
            }
        }
    }
}
