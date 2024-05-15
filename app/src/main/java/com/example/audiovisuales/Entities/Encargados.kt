package com.example.audiovisuales.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "encargados")
data class Encargados (
    @PrimaryKey val cedulaEncargado: String,
    val nombres: String,
    val cargo: String,
    val clave: String
): Serializable