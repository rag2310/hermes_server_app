package com.rago.features.authentication.model

import java.util.*

data class ManagementsDto(
    val id: UUID,
    val type: Int,
    val param: Long,
    val status: Int,
    val idUser: UUID
)
