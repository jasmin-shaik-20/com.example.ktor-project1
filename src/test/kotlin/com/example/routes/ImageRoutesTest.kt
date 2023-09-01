package com.example.routes

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import java.io.ByteArrayInputStream
import java.util.*
import kotlin.test.Test

class ImageRoutesTest {

    @OptIn(InternalAPI::class)
    @Test
    fun testPostImage()= testApplication {
        val imageUrl="https://random.dog/1f1447c8-0eda-4a0e-8e02-d8194243737c.png"
        val imageByteArray = "dummyImageData".toByteArray()

        ImageRequest.downloadImage = { _ ->
            ByteArrayInputStream(imageByteArray)
        }

        client.post("/postural/getBase64FromImageUrl") {
            headers[HttpHeaders.ContentType] = ContentType.Application.FormUrlEncoded.toString()
            body = MultiPartFormDataContent(
                formData {
                    append("imageUrl", imageUrl)
                }
            )
        }
        Base64.getEncoder().encodeToString(imageByteArray)

    }
    object ImageRequest {
        var downloadImage: (String) -> ByteArrayInputStream = { _ -> TODO() }
    }


}