package com.rago.features.authentication.routes

import com.rago.features.authentication.jwt.JwtConfig
import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.routes.changePassword.changePassword
import com.rago.features.authentication.routes.createUser.createUser
import com.rago.features.authentication.routes.loginUser.loginUser
import com.rago.features.authentication.routes.restorePassword.restorePassword
import com.rago.utils.secure.SecureEncryptionImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.authenticationRoutes() {
    routing {
        loginUser()
        createUser()
        restorePassword()
        authenticate(*JwtConfig.getAllRoles().toTypedArray()){
            changePassword()
        }
    }
}