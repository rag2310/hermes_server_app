package com.rago.features.authentication.jwt

import com.auth0.jwt.JWTVerifier
import com.rago.features.authentication.model.Rol
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

object JwtConfig {
    const val ROL_ADMIN = "ADMIN"
    const val ROL_CLIENT = "CLIENT"
    const val ROL_DEALER = "DEALER"
    const val ROL_USERS = "USERS"

    private fun validateRol(jwtCredential: JWTCredential, rolJwt: Int): JWTPrincipal? {
        return if (jwtCredential.payload.getClaim("rol").asInt() == rolJwt) {
            JWTPrincipal(jwtCredential.payload)
        } else {
            null
        }
    }

    private fun JWTAuthenticationProvider.Config.validateRol(verifier: JWTVerifier, rol: Int) {
        verifier(verifier)
        validate { jwtCredential ->
            validateRol(jwtCredential, rol)
        }
        challenge { _, realm ->
            call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired $realm")
        }
    }

    fun AuthenticationConfig.jwtAdmin(verifier: JWTVerifier) {
        jwt(ROL_ADMIN) {
            validateRol(verifier, Rol.ADMIN.ordinal)
        }
    }

    fun AuthenticationConfig.jwtClient(verifier: JWTVerifier) {
        jwt(ROL_CLIENT) {
            validateRol(verifier, Rol.CLIENT.ordinal)
        }
    }

    fun AuthenticationConfig.jwtDealer(verifier: JWTVerifier) {
        jwt(ROL_DEALER) {
            validateRol(verifier, Rol.DEALER.ordinal)
        }
    }

    fun AuthenticationConfig.jwtUsers(verifier: JWTVerifier) {
        jwt(ROL_USERS) {
            validateRol(verifier, Rol.USERS.ordinal)
        }
    }
}