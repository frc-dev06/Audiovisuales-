package com.example.audiovisuales.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.audiovisuales.Entities.Encargados

@Dao
interface EncargadosDAO {

    @Query("SELECT * FROM encargados WHERE cedulaEncargado = :cedula")
    fun seleccionarEncargadoPorCedula(cedula: String): Encargados?

    @Insert
    fun insertarEncargado(encargado: Encargados)

    @Update
    fun actualizarEncargado(encargado: Encargados)

    @Delete
    fun borrarEncargado(encargado: Encargados)
}