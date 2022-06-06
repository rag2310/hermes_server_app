package com.rago.features.authentication.dao

import com.rago.features.authentication.dao.entity.Roles
import com.rago.features.authentication.dao.entity.Users
import com.rago.features.authentication.dao.mapper.fromUserDaoToUserInfo
import com.rago.features.authentication.dao.mapper.fromUserDaoToUserInfoRol
import com.rago.features.authentication.jwt.JwtManager
import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import com.rago.features.authentication.model.UsersStatus
import com.rago.model.FlowResult
import com.rago.utils.secure.SecureEncryption
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class AuthenticationDaoImpl(
    private val secureEncryption: SecureEncryption,
    private val jwtManager: JwtManager
) : AuthenticationDao {
    override fun login(request: LoginRequestDto): Flow<FlowResult<UserInfoDto>> =
        flow {
            val result = transaction {
                val user = Users.select {
                    Users.username eq request.username
                }.singleOrNull()

                if (user == null) {
                    return@transaction FlowResult<UserInfoDto>(
                        error = "Bad credentials",
                        data = null,
                        code = HttpStatusCode.BadRequest.value
                    )
                } else {
                    val userToken = user.fromUserDaoToUserInfo()
                    val decryptPassword = secureEncryption.decryptString(userToken.password!!)
                    if (decryptPassword == request.password) {
                        userToken.token = jwtManager.generateToken(userToken)
                        return@transaction FlowResult(
                            data = userToken.apply {
                                password = null
                            },
                            code = HttpStatusCode.OK.value
                        )
                    } else {
                        return@transaction FlowResult<UserInfoDto>(
                            error = "Bad credentials",
                            data = null,
                            code = HttpStatusCode.BadRequest.value
                        )
                    }
                }
            }
            emit(result)
        }

    override fun getUserInfo(username: String): UserInfoDto {
        val userInfo = transaction {
            return@transaction Users.select {
                Users.username eq username
            }.single().fromUserDaoToUserInfo()
        }
        return userInfo
    }

    override fun createUser(userInfoDto: UserInfoDto): Flow<FlowResult<UserInfoDto>> = flow {
        val result: FlowResult<UserInfoDto> = transaction {
            val user = Users.select {
                (Users.username eq userInfoDto.username) or (Users.email eq userInfoDto.email)
            }.singleOrNull()

            if (user != null) {
                return@transaction FlowResult<UserInfoDto>(
                    error = "Username or Email already exists",
                    data = null,
                    code = HttpStatusCode.BadRequest.value
                )
            } else {
                val row = Users.insert {
                    it[id] = UUID.randomUUID()
                    it[username] = userInfoDto.username
                    it[email] = userInfoDto.email
                    it[password] = secureEncryption.encryptString(userInfoDto.password!!)
                    it[idRol] = userInfoDto.idRol
                }

                if (row.insertedCount > 0) {
                    return@transaction FlowResult(
                        error = null,
                        data = userInfoDto.apply {
                            password = null
                        },
                        code = HttpStatusCode.Created.value
                    )
                } else {
                    return@transaction FlowResult<UserInfoDto>(
                        error = "Error inserting into database",
                        data = null,
                        code = HttpStatusCode.BadGateway.value
                    )
                }
            }
        }

        emit(result)
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

    override fun getUserInfoByEmail(email: String): UserInfoDto? {
        val userInfo = transaction {
            return@transaction Users.select {
                (Users.email eq email) and (Users.status eq UsersStatus.US_001.status)
            }.singleOrNull()?.fromUserDaoToUserInfo()
        }
        return userInfo
    }

    override fun updateStatus(id: UUID, status: UsersStatus) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[this.status] = status.status
            }
        }
    }

    override fun changePassword(id: UUID, newPassword: String): UserInfoDto? {
        return transaction {
            val update = Users.update({ (Users.id eq id) and (Users.status eq UsersStatus.US_002.status) }) {
                it[password] = newPassword
                it[status] = UsersStatus.US_003.status
            }
            return@transaction if (update > 0) {
                Users.select {
                    (Users.id eq id)
                }.singleOrNull()?.fromUserDaoToUserInfo()
            } else null
        }
    }

    override fun updatePassword(username: String, oldPassword: String, newPassword: String): UserInfoDto? {
        return transaction {

            val user = Users.select {
                (Users.username eq username)
            }.singleOrNull()?.fromUserDaoToUserInfo()

            if (user != null) {
                if (oldPassword == secureEncryption.decryptString(user.password!!)) {

                    val encryptionPassword = secureEncryption.encryptString(newPassword)
                    val update = Users.update({
                        (Users.username eq username)
                    }) {
                        it[password] = encryptionPassword
                        it[status] = UsersStatus.US_001.status
                    }

                    user.password = null
                    user.status = UsersStatus.US_001.status
                    return@transaction if (update > 0) {
                        user
                    } else null
                } else {
                    return@transaction null
                }
            } else {
                return@transaction null
            }
        }
    }
}