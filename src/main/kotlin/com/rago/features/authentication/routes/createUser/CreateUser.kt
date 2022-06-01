package com.rago.features.authentication.routes.createUser

import com.rago.features.authentication.data.AuthenticationData
import com.rago.features.authentication.model.UserInfoDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.createUser() {
    val authenticationData: AuthenticationData by inject()
    post("/authentication/createUser") {
        authenticationData.createUser(call.receive<UserInfoDto>())
        call.respond(HttpStatusCode.Created)
    }
}