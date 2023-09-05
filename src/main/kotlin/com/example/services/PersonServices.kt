package com.example.services

import com.example.entities.PersonEntity
import com.example.repository.PersonRepositoryImpl
import com.example.repository.ProductRepositoryImpl
import com.example.utils.appConstants.GlobalConstants.TIME
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import redis.clients.jedis.Jedis
import java.util.*

class PersonServices:KoinComponent {

    private val personRepositoryImpl by inject<PersonRepositoryImpl>()

    suspend fun handlePostPersonDetails(post: PersonEntity): Any {
        return personRepositoryImpl.createPerson(post.name)
    }

    suspend fun handleGetDataFromCacheOrSource(
        personRepositoryImpl: PersonRepositoryImpl,
        jedis: Jedis,
        id: UUID
    ): Any {
        val cachedData = jedis.get("my_cached_data")
        return if (cachedData != null) {
            "Cached data: $cachedData"
        } else {
            val dataFromSource = personRepositoryImpl.getPersonById(id)
            jedis.setex("my_cached_data", TIME, dataFromSource)
            "Data from source: $dataFromSource"
        }
    }
}
