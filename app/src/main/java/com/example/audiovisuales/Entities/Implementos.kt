package com.example.audiovisuales.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName= "implementos")
data class Implementos (
    @PrimaryKey(autoGenerate = true)
    val codigo: Long=0,
    val nombre: String,
    val caracteristicas: String
):Serializable