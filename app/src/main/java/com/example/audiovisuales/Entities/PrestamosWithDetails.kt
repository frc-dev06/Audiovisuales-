package com.example.audiovisuales.Entities

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class PrestamosWithDetails (
    @Embedded val prestamo: Prestamos,
    @Relation(
        parentColumn = "cedulaEncargado",
        entityColumn = "cedulaEncargado"
    )
    val encargado: Encargados,

    @Relation(
        parentColumn = "codigoImplemento",
        entityColumn = "codigo"
    )
    val implemento: Implementos,

    @Relation(
        parentColumn = "cedulaDocente",
        entityColumn = "cedulaDocente"
    )
    val docente: Docentes
):Serializable
