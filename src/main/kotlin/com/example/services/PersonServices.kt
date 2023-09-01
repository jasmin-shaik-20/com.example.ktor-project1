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

    private val personRepository=PersonRepository()
    suspend fun handlePostPersonDetails(post: Person): Any? {
        val person = personRepository.createPersonData(post.id, post.name)
        return person
    }

    suspend fun handleGetDataFromCacheOrSource(
        personRepository: PersonRepository,
        jedis: Jedis,
        id: Int?
    ): Any {
        val cachedData = jedis.get("my_cached_data")
        if (cachedData != null) {
            return "Cached data: $cachedData"
        } else {
            if (id == null) {
                return HttpStatusCode.BadRequest to "Invalid 'id' parameter"
            }
            val dataFromSource = personRepository.fetchData(id)
            jedis.setex("my_cached_data", TIME, dataFromSource)
            return "Data from source: $dataFromSource"
        }
    }
}
