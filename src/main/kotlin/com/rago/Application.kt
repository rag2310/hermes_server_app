package com.rago

import com.rago.data.database.DatabaseFactory
import com.rago.plugins.configureAuthentication
import com.rago.plugins.configureContentGson
import com.rago.plugins.configureKoin
import com.rago.plugins.configureRouting
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val port = config.property("deployment.port").getString().toInt()
    val host = config.property("deployment.host").getString()


    embeddedServer(Netty, port = port, host = host) {
        DatabaseFactory.init()
        configureKoin()
        configureRouting()
        configureAuthentication()
        configureContentGson()
    }.start(wait = true)
}
