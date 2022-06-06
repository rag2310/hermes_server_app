package com.rago.features.authentication.model

data class ChangePasswordRequestDto(
    val oldPassword: String,
    val newPassword: String
)