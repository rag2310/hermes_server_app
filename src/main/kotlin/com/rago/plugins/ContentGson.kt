package com.rago.plugins

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureContentGson(){
    install(ContentNegotiation) {
        gson()
    }
}