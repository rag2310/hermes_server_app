package com.rago.features.authentication.data

import com.rago.features.authentication.dao.AuthenticationDao
import com.rago.features.authentication.model.LoginRequestDto
import com.rago.features.authentication.model.UserInfoDto
import com.rago.features.authentication.model.UserInfoRolDto
import com.rago.features.authentication.model.UsersStatus
import com.rago.model.FlowResult
import com.rago.utils.secure.SecureEncryption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import java.util.*

class AuthenticationDataImpl(
    private val authenticationDao: AuthenticationDao,
    private val secureEncryption: SecureEncryption
) : AuthenticationData {
    override fun login(request: LoginRequestDto): Flow<FlowResult<UserInfoDto>> {
        return authenticationDao.login(request)
    }

    override fun getUserInfo(username: String): UserInfoDto {
        return authenticationDao.getUserInfo(username).apply {
            password = null
        }
    }

    override fun createUser(userInfoDto: UserInfoDto): Flow<FlowResult<UserInfoDto>> {
        return authenticationDao.createUser(userInfoDto)
    }

    override fun getUserRolInfo(username: String): UserInfoRolDto {
        return authenticationDao.getUserRolInfo(username)
    }

    override fun restorePassword(email: String): UserInfoDto? {
        val user = authenticationDao.getUserInfoByEmail(email)
        if (user != null) {
            val mailToSend = HtmlEmail()
            mailToSend.hostName = "smtp.gmail.com"
            mailToSend.setSmtpPort(587)
            mailToSend.setAuthenticator(DefaultAuthenticator("hermes.app.server@gmail.com", "mstcsjwdadhszmrj"))
            mailToSend.setDebug(true)
            mailToSend.mailSession.properties["mail.smtps.auth"] = "true";
            mailToSend.mailSession.properties["mail.debug"] = "true";
            mailToSend.mailSession.properties["mail.smtps.port"] = "587";
            mailToSend.mailSession.properties["mail.smtps.socketFactory.port"] = "587";
            mailToSend.mailSession.properties["mail.smtps.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory";
            mailToSend.mailSession.properties["mail.smtps.socketFactory.fallback"] = "false";
            mailToSend.mailSession.properties["mail.smtp.starttls.enable"] = "true";
            mailToSend.setFrom("hermes.app.server@gmail.com", "Hermes App")
            mailToSend.subject = "Restore Password"

            val url = "http://192.168.0.105:8080/authentication/restorePassword/${user.id}"

            mailToSend.setHtmlMsg(
                "<h1>Hermes App</h1>\n" +
                        "\n" +
                        "<p>Password change is requested enter the following link to reset.</p>\n" +
                        "\n" +
                        "<p><a href=\"$url\">Click here!</a></p>\n" +
                        "\n" +
                        "<p>&nbsp;</p>"
            );
            mailToSend.addTo(email)
            mailToSend.send()
        }

        return user
    }

    override fun changedPassword(id: String): Flow<UserInfoDto?> = flow {

        val newPassword = (1..10)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        val user =
            authenticationDao.changePassword(UUID.fromString(id), secureEncryption.encryptString(newPassword))
        if (user != null) {
            emit(user)
            val mailToSend = HtmlEmail()
            mailToSend.hostName = "smtp.gmail.com"
            mailToSend.setSmtpPort(587)
            mailToSend.setAuthenticator(DefaultAuthenticator("hermes.app.server@gmail.com", "mstcsjwdadhszmrj"))
            mailToSend.setDebug(true)
            mailToSend.mailSession.properties["mail.smtps.auth"] = "true";
            mailToSend.mailSession.properties["mail.debug"] = "true";
            mailToSend.mailSession.properties["mail.smtps.port"] = "587";
            mailToSend.mailSession.properties["mail.smtps.socketFactory.port"] = "587";
            mailToSend.mailSession.properties["mail.smtps.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory";
            mailToSend.mailSession.properties["mail.smtps.socketFactory.fallback"] = "false";
            mailToSend.mailSession.properties["mail.smtp.starttls.enable"] = "true";
            mailToSend.setFrom("hermes.app.server@gmail.com", "Hermes App")
            mailToSend.subject = "Restore Password"
            mailToSend.setHtmlMsg(
                "<h1>Hermes App</h1>\n" +
                        "\n" +
                        "<p>Your password has been changed please you are asked to change it from the app.</p>\n" +
                        "\n" +
                        "<h6>New Password:</h6> <p>${secureEncryption.decryptString(user.password!!)}</p>\n" +
                        "\n" +
                        "<p>&nbsp;</p>"
            );
            mailToSend.addTo(user.email)
            mailToSend.send()
        }
        emit(null)
    }

    override fun updateStatus(id: UUID, usersStatus: UsersStatus) {
        authenticationDao.updateStatus(id, usersStatus)
    }

    override fun changePasswordByUsername(
        username: String,
        oldPassword: String,
        newPassword: String
    ): Flow<UserInfoDto?> = flow {
        val user = authenticationDao.updatePassword(username, oldPassword, newPassword)
        emit(user)
    }


    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
}