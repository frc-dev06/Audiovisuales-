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

}

