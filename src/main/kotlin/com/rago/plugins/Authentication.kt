package com.rago.plugins

import com.rago.features.authentication.jwt.JwtConfig.jwtAdmin
import com.rago.features.authentication.jwt.JwtConfig.jwtClient
import com.rago.features.authentication.jwt.JwtConfig.jwtDealer
import com.rago.features.authentication.jwt.JwtConfig.jwtUsers
import com.rago.features.authentication.jwt.JwtManager
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtManager: JwtManager by inject()
    install(Authentication) {
        jwtAdmin(jwtManager.getVerifier())
        jwtClient(jwtManager.getVerifier())
        jwtDealer(jwtManager.getVerifier())
        jwtUsers(jwtManager.getVerifier())
    }
}