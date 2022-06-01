package com.rago.features.authentication.dao

import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto

interface AuthenticationDao {
    fun login(request: LoginRequestDto): UserInfoDto?
    fun getUserInfo(username: String): UserInfoDto
    fun createUser(userInfoDto: UserInfoDto)
    fun getUserRolInfo(username: String): UserInfoRolDto
}