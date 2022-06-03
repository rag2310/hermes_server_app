package com.rago.features.authentication.dao

import com.rago.features.authentication.dao.entity.Managements
import com.rago.features.authentication.dao.entity.Roles
import com.rago.features.authentication.dao.entity.Users
import com.rago.features.authentication.dao.mapper.fromManagements
import com.rago.features.authentication.dao.mapper.fromUserDaoToUserInfo
import com.rago.features.authentication.dao.mapper.fromUserDaoToUserInfoRol
import com.rago.features.authentication.jwt.JwtManager
import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.ManagementsDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import com.rago.model.FlowResult
import com.rago.utils.secure.SecureEncryption
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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
                Users.email eq email
            }.singleOrNull()?.fromUserDaoToUserInfo()
        }
        return userInfo
    }

    override fun createManagement(userInfoDto: UserInfoDto): ManagementsDto? {
        val managements = transaction {
            Managements.insert {
                it[id] = UUID.randomUUID()
                it[type] = 0
                it[param] = Date().time
                it[status] = 0
                it[idUser] = userInfoDto.id
            }
        }

        if (managements.resultedValues != null) {
            if (managements.resultedValues!!.isNotEmpty()) {
                return managements.resultedValues!![0].fromManagements()
            }
        }
        return null
    }

    override fun getManagement(id: UUID): UserInfoDto? {
        val userInfoDto = transaction {
            val managementsDto = Managements.select {
                (Managements.id eq id)
            }.singleOrNull()?.fromManagements()

            if (managementsDto != null) {
                Users.update({ Users.id eq managementsDto.idUser }) {
                    val newPassword = (1..10)
                        .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString("")
                    it[password] = secureEncryption.encryptString(newPassword)
                }

                val users = Users.select {
                    (Users.id eq managementsDto.idUser)
                }.singleOrNull()?.fromUserDaoToUserInfo()

                if (users != null) {
                    return@transaction users
                } else {
                    return@transaction null
                }
            } else {
                return@transaction null
            }
        }
        return userInfoDto
    }

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
}