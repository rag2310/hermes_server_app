package com.rago.features.authentication.di

import com.rago.features.authentication.dao.AuthenticationDao
import com.rago.features.authentication.dao.AuthenticationDaoImpl
import com.rago.features.authentication.data.AuthenticationData
import com.rago.features.authentication.data.AuthenticationDataImpl
import com.rago.features.authentication.jwt.JwtManager
import com.rago.features.authentication.jwt.JwtManagerImpl
import com.typesafe.config.ConfigFactory
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val authenticationModule = module {
    single { JwtManagerImpl(get(qualifier = named("jwtSecretProperty"))) } bind JwtManager::class
    single(named("jwtSecretProperty")) {
        ConfigFactory.load().getString("jwt.secret").toString()
    }
    single { AuthenticationDaoImpl(get(),get()) } bind AuthenticationDao::class
    single { AuthenticationDataImpl(get(),get()) } bind AuthenticationData::class
}