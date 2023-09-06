package com.example.routes

import com.example.entities.PersonEntity
import com.example.repository.PersonRepositoryImpl
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
import java.util.*

fun Application.configurePersonRoutes() {
    val personRepositoryImpl: PersonRepositoryImpl by inject()
    val personServices : PersonServices by inject()
    val jedis: Jedis by inject()

routing {
    route(ApiEndPoints.PERSON) {
        post {
            val post = call.receive<PersonEntity>()
            val person = personServices.handlePostPersonDetails(post)
            call.respond(HttpStatusCode.OK, person)
        }

        get("/data-from-cache-or-source") {
            val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                .getOrNull()?:return@get call.respond("Missing id")
            val result = personServices.handleGetDataFromCacheOrSource(personRepositoryImpl, jedis, id)
            if (result is Pair<*, *>) {
                call.respond(result.first, result.second as TypeInfo)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        }
    }

    }
}
