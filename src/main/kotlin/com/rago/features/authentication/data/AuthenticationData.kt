package com.rago.features.authentication.data

import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import com.rago.features.authentication.model.UsersStatus
import com.rago.model.FlowResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface AuthenticationData {
    fun login(request: LoginRequestDto): Flow<FlowResult<UserInfoDto>>
    fun getUserInfo(username: String): UserInfoDto
    fun createUser(userInfoDto: UserInfoDto): Flow<FlowResult<UserInfoDto>>
    fun getUserRolInfo(username: String): UserInfoRolDto
    fun restorePassword(email: String): UserInfoDto?
    fun changedPassword(id: String): Flow<UserInfoDto?>
    fun updateStatus(id: UUID, usersStatus: UsersStatus)
    fun changePasswordByUsername(username: String, oldPassword: String, newPassword: String): Flow<UserInfoDto?>
}