package com.rago.features.authentication.jwt

import com.auth0.jwt.JWTVerifier
import com.rago.features.authentication.model.UserInfoDto
import java.util.*

interface JwtManager {
    fun generateToken(loginRequestDto: UserInfoDto): String
    fun getExpiration(): Date
    fun getVerifier(): JWTVerifier
    fun getUsernameFromToken(token: String?): String?
}