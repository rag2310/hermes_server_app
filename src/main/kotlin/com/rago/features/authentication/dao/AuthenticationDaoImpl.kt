package com.rago.features.authentication.dao

import com.rago.features.authentication.dao.entity.Roles
import com.rago.features.authentication.dao.entity.Users
import com.rago.features.authentication.dao.mapper.fromUserDaoToUserInfo
import com.rago.features.authentication.dao.mapper.fromUserDaoToUserInfoRol
import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class AuthenticationDaoImpl : AuthenticationDao {
    override fun login(request: LoginRequestDto): UserInfoDto? {
        return transaction {
            return@transaction Users.select {
                Users.username eq request.username
            }.singleOrNull()
        }?.fromUserDaoToUserInfo()
    }

    override fun getUserInfo(username: String): UserInfoDto {
        val userInfo = transaction {
            return@transaction Users.select {
                Users.username eq username
            }.single().fromUserDaoToUserInfo()
        }
        return userInfo
    }

    override fun createUser(userInfoDto: UserInfoDto) {
        transaction {
            Users.insert {
                it[id] = UUID.randomUUID()
                it[username] = userInfoDto.username
                it[email] = userInfoDto.email
                it[password] = userInfoDto.password!!
                it[idRol] = userInfoDto.idRol
            }
        }
    }

    override fun getUserRolInfo(username: String): UserInfoRolDto {
        return transaction {
            return@transaction (Users innerJoin Roles).slice(
                Users.username,
                Users.idRol,
                Roles.description
            ).select {
                (Users.username eq username)
            }.single().fromUserDaoToUserInfoRol()
        }
    }
}