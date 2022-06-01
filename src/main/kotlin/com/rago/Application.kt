package com.rago

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.rago.data.database.DatabaseFactory
import com.rago.plugins.configureRouting
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*

fun main() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val port = config.property("deployment.port").getString().toInt()
    val host = config.property("deployment.host").getString()
    val secret = config.property("jwt.secret").getString()
    val issuer = config.property("jwt.issuer").getString()
    val audience = config.property("jwt.audience").getString()
    val myRealm = config.property("jwt.realm").getString()
    embeddedServer(Netty, port = port, host = host) {
        DatabaseFactory.init()
        configureRouting()
        install(ContentNegotiation) {
            gson()
        }
        install(Authentication) {
            jwt("auth-jwt") {
                realm = myRealm
                verifier(
                    JWT.require(Algorithm.HMAC256(secret))
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.getClaim("rol").asInt() == 0) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }

                challenge { defaultScheme, realm ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
            jwt("auth-admin") {
                realm = myRealm
                verifier(
                    JWT.require(Algorithm.HMAC256(secret))
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.getClaim("rol").asInt() == 1) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }

                challenge { defaultScheme, realm ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
        }
    }.start(wait = true)
}
