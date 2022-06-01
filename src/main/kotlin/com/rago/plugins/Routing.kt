package com.rago.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.rago.domain.UserController
import com.rago.domain.model.UserLogin
import com.rago.domain.model.UserRequest
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRouting() {

    val config = HoconApplicationConfig(ConfigFactory.load())
    val secret = config.property("jwt.secret").getString()
    val issuer = config.property("jwt.issuer").getString()
    val audience = config.property("jwt.audience").getString()

    val userController = UserController()

    routing {
        route("/auth") {
            post("/login") {
                val userLogin = call.receive<UserLogin>()
                if (userLogin.username == "rag23" && userLogin.password == "123" || userLogin.username == "rag24" && userLogin.password == "123") {
                    val idRol = if (userLogin.username == "rag23") {
                        0
                    } else {
                        1
                    }
                    val token = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("rol", idRol)
                        .withClaim("username", userLogin.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                        .sign(Algorithm.HMAC256(secret))


                    call.respond(hashMapOf("token" to token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
            post("/register") {
                val userRequest = call.receive<UserRequest>()
                userController.insert(userRequest)
                call.respond(HttpStatusCode.Created)
            }
        }

        authenticate("auth-jwt", "auth-admin") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }

        authenticate("auth-admin") {
            get("/helloadmin") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                userController.getUserAndRol()
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}
