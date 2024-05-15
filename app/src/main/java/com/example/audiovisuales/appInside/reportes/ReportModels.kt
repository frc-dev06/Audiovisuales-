package com.example.audiovisuales.appInside.reportes

class ReportModels {
    data class PrestamoPorDia(
        val diaSemana: String?,
        val cantidad: Int
    );

    data class ReporteDocentePrestamos(
        val nombreDocente: String,
        val cantidadPrestamos: Int
    )

    data class ReporteImplementoPrestamos(
        val nombreImplemento: String,
        val cantidadPrestamos: Int
    )

}