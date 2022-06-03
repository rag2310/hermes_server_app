package com.rago.features.authentication.dao.mapper

import com.rago.features.authentication.dao.entity.Managements
import com.rago.features.authentication.dao.entity.Roles
import com.rago.features.authentication.dao.entity.Users
import com.rago.features.authentication.model.ManagementsDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.fromUserDaoToUserInfo() = UserInfoDto(
    id = this[Users.id],
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

fun ResultRow.fromManagements() = ManagementsDto(
    id = this[Managements.id],
    type = this[Managements.type],
    param = this[Managements.param],
    status = this[Managements.status],
    idUser = this[Managements.idUser]
)