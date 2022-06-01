package com.rago.features.authentication.data

import com.rago.features.authentication.dao.AuthenticationDao
import com.rago.features.authentication.jwt.JwtManager
import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto

class AuthenticationDataImpl(
    private val authenticationDao: AuthenticationDao,
    private val jwtManager: JwtManager
) : AuthenticationData {
    override fun login(request: LoginRequestDto): Map<String, String> {
        val user = authenticationDao.login(request)
        return if (user != null) {
            mapOf("token" to jwtManager.generateToken(user))
        } else {
            mapOf("msg" to "Bad Credentials")
        }
    }

    override fun getUserInfo(username: String): UserInfoDto {
        return authenticationDao.getUserInfo(username).apply {
            password = null
        }
    }

    override fun createUser(userInfoDto: UserInfoDto) {
        return authenticationDao.createUser(userInfoDto)
    }

    override fun getUserRolInfo(username: String): UserInfoRolDto {
        return authenticationDao.getUserRolInfo(username)
    }
}