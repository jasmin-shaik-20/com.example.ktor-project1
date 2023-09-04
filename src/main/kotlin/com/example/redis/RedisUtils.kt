package com.example.redis

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import redis.clients.jedis.JedisPool

object RedisUtils {
    private val config = HoconApplicationConfig(ConfigFactory.load())
    private val host = config.property("ktor.redis.host").getString()
    private val port = config.property("ktor.redis.port").getString().toInt()
    private val jedisPool = JedisPool(host, port)
    fun set(key: String, value: String, expireTime: Int) {
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