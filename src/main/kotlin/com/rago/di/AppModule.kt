package com.rago.di

import com.rago.utils.secure.SecureEncryption
import com.rago.utils.secure.SecureEncryptionImpl
import com.typesafe.config.ConfigFactory
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single { SecureEncryptionImpl(get(qualifier = named("secureKeyProperty"))) } bind SecureEncryption::class
    single(named("secureKeyProperty")) {
        ConfigFactory.load().getString("secure.key").toString()
    }
}