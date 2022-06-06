package com.rago.features.authentication.dao

import com.rago.features.authentication.model.*
import com.rago.model.FlowResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface AuthenticationDao {
    fun login(request: LoginRequestDto): Flow<FlowResult<UserInfoDto>>
    fun getUserInfo(username: String): UserInfoDto
    fun createUser(userInfoDto: UserInfoDto): Flow<FlowResult<UserInfoDto>>
    fun getUserRolInfo(username: String): UserInfoRolDto
    fun getUserInfoByEmail(email: String): UserInfoDto?
    fun updateStatus(id: UUID, status: UsersStatus)
    fun changePassword(id: UUID, newPassword: String): UserInfoDto?
    fun updatePassword(username: String, oldPassword: String, newPassword: String): UserInfoDto?
}