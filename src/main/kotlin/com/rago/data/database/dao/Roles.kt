package com.rago.data.database.dao

import org.jetbrains.exposed.sql.Table

object Roles : Table() {
    val id = integer("id")
    val description = text("description")

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "roles_pkey")
}