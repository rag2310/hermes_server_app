package com.rago.features.authentication.routes.restorePassword

import com.rago.features.authentication.data.AuthenticationData
import com.rago.features.authentication.model.RestorePasswordDto
import com.rago.features.authentication.model.UsersStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.collect
import org.koin.ktor.ext.inject

fun Route.restorePassword() {
    val authenticationData: AuthenticationData by inject()
    post("/authentication/restorePassword") {
        val request = call.receive<RestorePasswordDto>()
        val user = authenticationData.restorePassword(request.email)
        if (user != null) {
            call.respond(HttpStatusCode.OK, "Email sent")
            authenticationData.updateStatus(user.id, UsersStatus.US_002)
        } else {
            call.respond(
                HttpStatusCode.BadRequest,
                "This email does not exist or a temporary password has already been sent to you."
            )
        }
    }

    get("/authentication/restorePassword/{id}") {
        val id = call.parameters["id"]
        authenticationData.changedPassword(id!!).collect {
            if (it != null)
                call.respond(HttpStatusCode.OK, "Email sent")
            else
                call.respond(HttpStatusCode.OK, "The link has expired or has already been used")
        }
    }
}