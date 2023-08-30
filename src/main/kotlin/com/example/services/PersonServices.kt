package com.example.services

import com.example.dao.Person
import com.example.endpoints.ApiEndPoint.TIME
import com.example.repository.PersonRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import redis.clients.jedis.Jedis

class PersonServices {
    suspend fun handlePostPersonDetails(call: ApplicationCall, personRepository: PersonRepository) {
        val post = call.receive<Person>()
        val person = personRepository.createPersonData(post.id, post.name)
        if (person != null) {
            call.respond(HttpStatusCode.OK, person)
        }
    }

    suspend fun handleGetDataFromCacheOrSource(call: ApplicationCall,
                                               personRepository: PersonRepository,
                                               jedis: Jedis
    ) {
        val cachedData = jedis.get("my_cached_data")
        if (cachedData != null) {
            call.respond(HttpStatusCode.OK, "Cached data: $cachedData")
        } else {
            val id = call.request.queryParameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid 'id' parameter")
                return
            }
            val dataFromSource = personRepository.fetchData(id)
            jedis.setex("my_cached_data", TIME, dataFromSource)
            call.respond(HttpStatusCode.OK, "Data from source: $dataFromSource")
        }
    }
}