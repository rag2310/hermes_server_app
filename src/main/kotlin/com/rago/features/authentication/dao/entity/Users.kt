package com.rago.features.authentication.dao.entity

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = uuid("id")
    val email = text("email")
    val password = text("password")
    val username = text("username")
    val idRol = (integer("id_rol") references Roles.id)

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "users_pkey")

}