package com.rago

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.rago.data.database.DatabaseFactory
import com.rago.features.authentication.di.authenticationModule
import com.rago.features.authentication.jwt.JwtManager
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
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val port = config.property("deployment.port").getString().toInt()
    val host = config.property("deployment.host").getString()
    val myRealm = config.property("jwt.realm").getString()



    embeddedServer(Netty, port = port, host = host) {

        DatabaseFactory.init()
        val jwtManager: JwtManager by inject()

        configureRouting()
        install(Koin) {
            modules(authenticationModule)
        }
        install(ContentNegotiation) {
            gson()
        }
        install(Authentication) {
            jwt("auth-jwt") {
                /*realm = myRealm*/
                verifier(jwtManager.getVerifier())
                validate { credential ->
                    if (credential.payload.getClaim("rol").asInt() == 2) {
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
                verifier(jwtManager.getVerifier())
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
