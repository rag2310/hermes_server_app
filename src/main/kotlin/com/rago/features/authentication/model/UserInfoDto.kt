package com.rago.features.authentication.model


data class UserInfoDto(
    val email: String,
    var password: String?,
    val username: String,
    val idRol: Int
)