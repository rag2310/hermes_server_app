package com.rago.features.authentication.routes.loginUser

import com.rago.features.authentication.data.AuthenticationData
import com.rago.features.authentication.model.LoginRequestDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.collect
import org.koin.ktor.ext.inject

fun Route.loginUser() {
    val authenticationData: AuthenticationData by inject()
    post("/authentication/login") {
        val request = call.receive<LoginRequestDto>()
        authenticationData.login(request).collect {
            call.respond(HttpStatusCode.OK,it)
        }
    }
}