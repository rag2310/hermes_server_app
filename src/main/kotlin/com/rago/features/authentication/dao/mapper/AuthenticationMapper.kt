package com.rago.features.authentication.dao.mapper

import com.rago.features.authentication.dao.entity.Roles
import com.rago.features.authentication.dao.entity.Users
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.fromUserDaoToUserInfo() = UserInfoDto(
    email = this[Users.email],
    password = this[Users.password],
    username = this[Users.username],
    idRol = this[Users.idRol],
)

fun ResultRow.fromUserDaoToUserInfoRol() = UserInfoRolDto(
    username = this[Users.username],
    idRol = this[Users.idRol],
    rolDescription = this[Roles.description]
)