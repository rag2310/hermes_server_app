package com.rago.features.authentication.routes

import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.routes.createUser.createUser
import com.rago.features.authentication.routes.loginUser.loginUser
import com.rago.features.authentication.routes.restorePassword.restorePassword
import com.rago.utils.secure.SecureEncryptionImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.authenticationRoutes() {
    routing {
        loginUser()
        createUser()
        restorePassword()
        /*post("/test") {
            val request = call.receive<LoginRequestDto>()
//            val encrypt = secureEncryption.encryptString(request.password)
            val decryp = secureEncryption.decryptString(request.password)
//            call.respond(HttpStatusCode.OK, hashMapOf("encrypt" to encrypt, "decryp" to decryp))
            call.respond(HttpStatusCode.OK, hashMapOf("password" to decryp))
        }*/
        /*authenticate("auth-jwt") {
            userInfo()
        }*/
        /*authenticate(JwtConfig.ROL_ADMIN) {
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
        }*/
    }
}