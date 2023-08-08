// File: UserSession.kt

package com.example.dao

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


object RedisUtils {
     val jedisPool = JedisPool("localhost",6379)


    fun set(key: String, value: String) {
        jedisPool.resource.use { jedis ->
            jedis.set(key, value)
        }
    }

    fun get(key: String): String? {
        return jedisPool.resource.use { jedis ->
            jedis.get(key)
        }
    }

    fun delete(key: String) {
        jedisPool.resource.use { jedis ->
            jedis.del(key)
        }
    }
    fun setWithExpiration(key: String, seconds: Int,value: String) {
        jedisPool.resource.use { jedis ->
            jedis.setex(key, seconds, value)
        }
    }

}