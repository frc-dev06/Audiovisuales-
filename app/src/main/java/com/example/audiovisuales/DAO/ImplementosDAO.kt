package com.example.audiovisuales.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.audiovisuales.Entities.Implementos

@Dao
interface ImplementosDAO {
    @Query("SELECT * FROM implementos")
    fun seleccionarTodoImplementos(): List<Implementos>

    @Query("SELECT * FROM implementos WHERE codigo = :codigo")
    fun seleccionarUnImplemento(codigo: Long): Implementos?

    @Insert
    fun insertarImplemento(implemento: Implementos)

    @Update
    fun actualizarImplemento(implemento: Implementos)

    @Query("DELETE FROM implementos WHERE codigo= :codigoInput")
    fun borrarImplemento(codigoInput: Long)

    @Query("""
    SELECT * FROM implementos
    WHERE codigo NOT IN (
        SELECT DISTINCT codigoImplemento FROM prestamos
        WHERE estado = 0 
    )
""")
    fun seleccionarImplementosDisponibles(): List<Implementos>

}