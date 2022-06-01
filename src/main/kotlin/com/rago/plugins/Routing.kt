package com.rago.plugins

import com.rago.features.authentication.routes.authenticationRoutes
import io.ktor.server.application.*

fun Application.configureRouting() {
    authenticationRoutes()
}
