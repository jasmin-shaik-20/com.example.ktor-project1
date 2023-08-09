package com.example.routes

import com.example.dao.ImageRequest
import com.example.file.ApiEndPoint
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureImageRoutes(){
    routing{
        route(ApiEndPoint.IMAGE){
            val imageRequest=ImageRequest()
            post("/getBase64FromImageUrl") {
                val multipart = call.receiveMultipart()
                var imageUrl: String? = null
                multipart.forEachPart { part ->
                    if (part is PartData.FormItem) {
                        if (part.name == "imageUrl") {
                            imageUrl = part.value
                        }
                    }
                    part.dispose()
                }

                if (imageUrl.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "Image URL not provided")
                    return@post
                }

                val imageByteArray = imageRequest.downloadImage(imageUrl!!)
                val base64Image = Base64.getEncoder().encodeToString(imageByteArray)
                call.respond(HttpStatusCode.OK, base64Image)
            }
        }
    }
}