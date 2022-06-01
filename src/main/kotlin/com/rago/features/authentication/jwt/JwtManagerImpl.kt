package com.rago.features.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.rago.features.authentication.model.UserInfoDto
import org.koin.core.component.KoinComponent
import java.util.*

class JwtManagerImpl(secret: String) : JwtManager, KoinComponent {
    private val validityInMs = 36_000_00 * 1
    private val algorithm = Algorithm.HMAC256(secret)

    override fun generateToken(loginRequestDto: UserInfoDto): String {

        return JWT.create()
            .withSubject("Authentication")
            .withClaim("username", loginRequestDto.username)
            .withClaim("rol", loginRequestDto.idRol)
            .withExpiresAt(getExpiration())
            .sign(algorithm)
    }

    override fun getExpiration(): Date = Date(System.currentTimeMillis() + validityInMs)

    override fun getVerifier(): JWTVerifier = JWT.require(algorithm).build()

    override fun getUsernameFromToken(token: String?): String? {
        return JWT.decode(token).getClaim("username").toString().replace("\"", "")
    }
}