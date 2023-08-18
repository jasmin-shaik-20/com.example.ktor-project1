package com.example.dao
import java.io.ByteArrayOutputStream
import java.net.URL
class ImageRequest {
    companion object {
        private const val BUFFER_SIZE = 1024
    }
    fun downloadImage(imageUrl: String): ByteArray {
        val connection = URL(imageUrl).openConnection()
        connection.connect()
        val inputStream = connection.getInputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }
        return byteArrayOutputStream.toByteArray()
    }
}
