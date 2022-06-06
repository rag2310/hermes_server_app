package com.rago.features.authentication.routes.changePassword

import com.rago.features.authentication.data.AuthenticationData
import com.rago.features.authentication.model.ChangePasswordRequestDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.collect
import org.koin.ktor.ext.inject

fun Route.changePassword() {
    val authenticationData: AuthenticationData by inject()
    post("/authentication/changePassword") {
        val principal = call.principal<JWTPrincipal>()
        val keys = call.receive<ChangePasswordRequestDto>()
        val (oldPassword, newPassword) = keys
        val username = principal!!.payload.getClaim("username").asString()
        authenticationData.changePasswordByUsername(username, oldPassword, newPassword).collect {
            call.respond(HttpStatusCode.OK, "The password has been updated")
        }
    }
}