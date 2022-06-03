package com.rago

import com.rago.data.database.DatabaseFactory
import com.rago.plugins.configureAuthentication
import com.rago.plugins.configureContentGson
import com.rago.plugins.configureKoin
import com.rago.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init()
    configureKoin()
    configureRouting()
    configureAuthentication()
    configureContentGson()
}
