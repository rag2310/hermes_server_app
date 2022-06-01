package com.rago.features.authentication.routes.userInfo

import com.rago.extensions.getAuthorizationTokenWithoutBearer
import com.rago.features.authentication.data.AuthenticationData
import com.rago.features.authentication.jwt.JwtManager
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userInfo() {
    val authenticationData: AuthenticationData by inject()
    val jwtManager: JwtManager by inject()
    get("/authentication/userInfo") {
        call.respond(
            authenticationData.getUserInfo(
                jwtManager.getUsernameFromToken(call.getAuthorizationTokenWithoutBearer())!!
            )
        )
    }

    get("/authentication/userInfoRol") {
        call.respond(
            authenticationData.getUserRolInfo(
                jwtManager.getUsernameFromToken(call.getAuthorizationTokenWithoutBearer())!!
            )
        )
    }
}