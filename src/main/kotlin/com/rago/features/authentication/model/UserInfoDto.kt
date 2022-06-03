package com.rago.features.authentication.model

import java.util.UUID


data class UserInfoDto(
    val id : UUID,
    val email: String,
    var password: String?,
    val username: String,
    val idRol: Int,
    var token: String? = null
)