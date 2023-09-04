package com.example.database.table

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import redis.clients.jedis.JedisPool
@Serializable
data class UserSession(val id:String,val username: String,val password:String) {
    fun toJson(): String = Json.encodeToString(this)
    companion object {
        fun fromJson(json: String): UserSession = Json.decodeFromString(json)
    }
}


