package com.example.services

import com.example.database.table.Person
import com.example.repository.PersonRepository
import com.example.utils.appConstants.GlobalConstants.TIME
import io.ktor.http.*
import redis.clients.jedis.Jedis

class PersonServices {

    private val personRepository=PersonRepository()
    suspend fun handlePostPersonDetails(post: Person): Any? {
        return personRepository.createPersonData(post.id, post.name)
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
