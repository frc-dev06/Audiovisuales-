package com.example.audiovisuales.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.audiovisuales.Entities.Docentes

@Dao
interface DocentesDAO {
    @Query("SELECT * FROM docentes")
    fun seleccionarTodoDocentes(): List<Docentes>

    @Query("SELECT * FROM docentes WHERE cedulaDocente = :cedula")
    fun seleccionarUnDocente(cedula: String): Docentes

    @Insert
    fun insertarDocente(docente: Docentes)

    @Update
    fun actualizarDocente(docente: Docentes)

    @Query("DELETE FROM docentes WHERE cedulaDocente = :cedula")
    fun borrarDocente(cedula: String)
}