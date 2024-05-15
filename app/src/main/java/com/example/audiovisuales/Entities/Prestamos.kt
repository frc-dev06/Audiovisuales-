package com.example.audiovisuales.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(tableName = "prestamos")
data class Prestamos(
    @PrimaryKey(autoGenerate = true)
    val idPrestamo: Long=0,
    val cedulaEncargado:String,
    val codigoImplemento: String,
    val cedulaDocente:String,

    val fechaHora:String,
    val estado: Int,
    val observaciones:String
): Serializable