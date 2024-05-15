package com.example.audiovisuales.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.example.audiovisuales.Entities.Docentes
import com.example.audiovisuales.Entities.Encargados
import com.example.audiovisuales.Entities.Implementos
import com.example.audiovisuales.Entities.Prestamos
import com.example.audiovisuales.Entities.PrestamosWithDetails
import com.example.audiovisuales.appInside.reportes.ReportModels

@Dao
interface PrestamosDAO {
    @Query("SELECT * FROM prestamos")
    fun seleccionarTodoPrestamos(): List<Prestamos>

    @Query("SELECT * FROM prestamos WHERE idPrestamo = :id")
    fun seleccionarPrestamoPorId(id: Long): Prestamos?

    @Insert
    fun insertarPrestamo(prestamo: Prestamos)

    @Update
    fun actualizarPrestamo(prestamo: Prestamos)

    @Query("DELETE FROM prestamos WHERE idPrestamo = :idPrestamo")
    fun borrarPrestamo(idPrestamo: Long)

    @Query("UPDATE prestamos SET estado = :nuevoEstado WHERE idPrestamo = :idPrestamo")
    fun actualizarEstadoPrestamo(idPrestamo: Long, nuevoEstado: Int)

    @Transaction
    @Query("SELECT * FROM prestamos ORDER BY fechaHora DESC")
    fun seleccionarDetallesPrestamo(): List<PrestamosWithDetails>

    @Query("""
    SELECT CASE strftime('%w', datetime(substr(fechaHora, 7, 4) || '-' || substr(fechaHora, 4, 2) || '-' || substr(fechaHora, 1, 2) || substr(fechaHora, 11, 9)))
        WHEN '0' THEN 'Domingo'
        WHEN '1' THEN 'Lunes'
        WHEN '2' THEN 'Martes'
        WHEN '3' THEN 'Miércoles'
        WHEN '4' THEN 'Jueves'
        WHEN '5' THEN 'Viernes'
        WHEN '6' THEN 'Sábado'
        ELSE 'Desconocido'
        END AS diaSemana, 
        COUNT(*) AS cantidad
    FROM prestamos
    GROUP BY diaSemana
""")
    fun obtenerCantidadPrestamosPorDiasSemana(): List<ReportModels.PrestamoPorDia>

    @Query("""
    SELECT docentes.nombres as nombreDocente, COUNT(prestamos.idPrestamo) as cantidadPrestamos
    FROM prestamos
    INNER JOIN docentes ON prestamos.cedulaDocente = docentes.cedulaDocente
    GROUP BY prestamos.cedulaDocente
""")
    fun obtenerCantidadPrestamosPorDocente(): List<ReportModels.ReporteDocentePrestamos>

    @Query("""
    SELECT implementos.nombre as nombreImplemento, COUNT(prestamos.idPrestamo) as cantidadPrestamos
    FROM prestamos
    INNER JOIN implementos ON prestamos.codigoImplemento = implementos.codigo
    GROUP BY prestamos.codigoImplemento
""")
    fun obtenerCantidadPrestamosPorImplemento(): List<ReportModels.ReporteImplementoPrestamos>


}

