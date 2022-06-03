package com.rago.features.authentication.dao.entity

import org.jetbrains.exposed.sql.Table

object Managements : Table("management") {
    val id = uuid("id")
    val type = integer("type")
    val param = long("param")
    val status = integer("status")
    val idUser = uuid("id_user")

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "management_pkey")
}
