package com.rago.features.authentication.routes

import com.rago.features.authentication.routes.createUser.createUser
import com.rago.features.authentication.routes.loginUser.loginUser
import com.rago.features.authentication.routes.userInfo.userInfo
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.authenticationRoutes() {
    routing {
        loginUser()
        authenticate("auth-jwt") {
            userInfo()
        }
        createUser()
    }
}