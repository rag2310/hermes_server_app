package com.rago.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

data class User(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val password: String,
    val username: String,
    val idRol: Int
)

@Serializable
data class UserRequest(
    val email: String,
    val password: String,
    val username: String,
    @SerialName("id_rol")
    val idRol: Int
)

@Serializable
data class UserLogin(
    val username: String,
    val password: String
)
