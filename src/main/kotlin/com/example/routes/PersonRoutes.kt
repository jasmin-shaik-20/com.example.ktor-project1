package com.example.routes

import com.example.database.table.Person
import com.example.repository.PersonRepository
import com.example.services.PersonServices
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import redis.clients.jedis.Jedis
import org.koin.ktor.ext.inject

fun Application.configurePersonRoutes() {
    val personRepository: PersonRepository by inject()
    val personServices = PersonServices()
    val jedis: Jedis by inject()

routing {
    route(ApiEndPoints.PERSON) {
        post {
            val post = call.receive<Person>()
            val person = personServices.handlePostPersonDetails(post)
            if (person != null) {
                call.respond(HttpStatusCode.OK, person)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/data-from-cache-or-source") {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            val result = personServices.handleGetDataFromCacheOrSource(personRepository, jedis, id)
            if (result is Pair<*, *>) {
                call.respond(result.first, result.second as TypeInfo)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        }
    }

    }
}
