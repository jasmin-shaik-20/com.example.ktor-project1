package com.example.routes

import com.example.dao.UserSession
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureUserSession(){
    routing{
        route("/session") {
            get("/login") {
                call.sessions.set(UserSession(username="Jasmin",password="Jas@20"))
                call.respondRedirect("/usersession")
            }
            get("/usersession") {
                val userSession = call.sessions.get<UserSession>()
                if (userSession != null) {
                    call.respondText("username is ${userSession.username}")
                } else {
                    call.respondText("Session doesn't exist or is expired.")
                }
            }
            get("/logout") {
                call.sessions.clear<UserSession>()
                call.respondText("Logout successful!")
            }
        }

    }
}