package com.example.routes

import com.example.database.table.ImageRequest
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.forEachPart
import io.ktor.http.content.PartData
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import java.util.Base64

fun Application.configureImageRoutes(){

    routing{

        route(ApiEndPoints.IMAGE){

            val imageRequest= ImageRequest()
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
