package com.rago.features.authentication.dao

import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.ManagementsDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import com.rago.model.FlowResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface AuthenticationDao {
    fun login(request: LoginRequestDto): Flow<FlowResult<UserInfoDto>>
    fun getUserInfo(username: String): UserInfoDto
    fun createUser(userInfoDto: UserInfoDto): Flow<FlowResult<UserInfoDto>>
    fun getUserRolInfo(username: String): UserInfoRolDto
    fun getUserInfoByEmail(email: String): UserInfoDto?
    fun createManagement(userInfoDto: UserInfoDto): ManagementsDto?
    fun getManagement(id: UUID): UserInfoDto?
}