package com.rago.domain

import com.rago.data.database.dao.Roles
import com.rago.data.database.dao.Users
import com.rago.domain.model.Rol
import com.rago.domain.model.User
import com.rago.domain.model.UserRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserController {

    fun getUserAndRol() {
        transaction {
            (Users innerJoin Roles).slice(Roles.id,Roles.description,Users.username)
                .select {
                    (Users.username eq "rag23")
                }.forEach {
                    println("${it[Users.username]} is ${it[Roles.id]}-${it[Roles.description]}")
                }
        }
    }

    fun insert(userRequest: UserRequest) {
        transaction {
            Users.insert {
                it[id] = UUID.randomUUID()
                it[email] = userRequest.email
                it[password] = userRequest.password
                it[username] = userRequest.username
                it[idRol] = userRequest.idRol
            }
        }
    }
}