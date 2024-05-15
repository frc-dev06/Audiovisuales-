package com.example.audiovisuales.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "docentes")
data class Docentes(
    @PrimaryKey val cedulaDocente: String,
    val nombres: String,
    val facultad: String
):Serializable