package com.example.dao

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.get
import io.ktor.http.*
import io.ktor.server.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.*

class ImageRequest {

    fun downloadImage(imageUrl: String): ByteArray {
        val connection = URL(imageUrl).openConnection()
        connection.connect()
        val inputStream = connection.getInputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }

        return byteArrayOutputStream.toByteArray()
    }
}
