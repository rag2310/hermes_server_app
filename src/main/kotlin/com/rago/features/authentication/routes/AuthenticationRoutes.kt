package com.rago.features.authentication.routes

import com.rago.features.authentication.jwt.JwtConfig
import com.rago.features.authentication.routes.createUser.createUser
import com.rago.features.authentication.routes.loginUser.loginUser
import com.rago.features.authentication.routes.userInfo.userInfo
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authenticationRoutes() {
    routing {
        loginUser()
        authenticate("auth-jwt") {
            userInfo()
        }
        createUser()
        authenticate(JwtConfig.ROL_ADMIN) {
            get("/admin") {
                call.respond("Admin")
            }
        }
        authenticate(JwtConfig.ROL_CLIENT) {
            get("/client") {
                call.respond("client")
            }
        }
        authenticate(JwtConfig.ROL_DEALER) {
            get("/dealer") {
                call.respond("dealer")
            }
        }
        authenticate(JwtConfig.ROL_USERS) {
            get("/users") {
                call.respond("users")
            }
        }
    }
}