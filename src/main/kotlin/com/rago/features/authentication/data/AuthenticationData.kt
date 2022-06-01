package com.rago.features.authentication.data

import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto

interface AuthenticationData {
    fun login(request: LoginRequestDto): Map<String,String>
    fun getUserInfo(username:String): UserInfoDto
    fun createUser(userInfoDto: UserInfoDto)
    fun getUserRolInfo(username: String): UserInfoRolDto
}