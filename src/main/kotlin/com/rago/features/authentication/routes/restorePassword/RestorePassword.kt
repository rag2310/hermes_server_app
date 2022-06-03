package com.rago.features.authentication.routes.restorePassword

import com.rago.features.authentication.data.AuthenticationData
import com.rago.features.authentication.model.RestorePasswordDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.restorePassword() {
    val authenticationData: AuthenticationData by inject()
    post("/authentication/restorePassword") {
        val request = call.receive<RestorePasswordDto>()
        val user = authenticationData.restorePassword(request.email)
        if (user != null) {
            call.respond(HttpStatusCode.OK, "Send email")
        } else {
            call.respond(HttpStatusCode.BadRequest, "Email address does not exist.")
        }
    }

    get("/authentication/restorePassword/{id}") {
        val id = call.parameters["id"]
        authenticationData.changedPassword(UUID.fromString(id))
        call.respond(HttpStatusCode.OK,"Send email")
    }
}