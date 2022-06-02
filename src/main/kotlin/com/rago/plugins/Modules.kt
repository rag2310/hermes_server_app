package com.rago.plugins

import com.rago.di.appModule
import com.rago.features.authentication.di.authenticationModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(appModule, authenticationModule)
    }
}