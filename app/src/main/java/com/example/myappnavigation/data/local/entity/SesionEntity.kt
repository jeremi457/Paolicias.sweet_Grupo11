package com.example.myappnavigation.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesion")
data class SesionEntity(
    @PrimaryKey
    val id: Int = 1,
    val rol: String,
    val nombre: String,
    val email: String,
    val token: String?
)
