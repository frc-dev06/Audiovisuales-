package com.example.audiovisuales.Configuracion

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.audiovisuales.DAO.DocentesDAO
import com.example.audiovisuales.DAO.EncargadosDAO
import com.example.audiovisuales.DAO.ImplementosDAO
import com.example.audiovisuales.DAO.PrestamosDAO
import com.example.audiovisuales.Entities.Docentes
import com.example.audiovisuales.Entities.Encargados
import com.example.audiovisuales.Entities.Implementos
import com.example.audiovisuales.Entities.Prestamos

@Database(entities = [Docentes::class,
    Implementos::class,
    Encargados::class,
    Prestamos::class],
    version=1
)
abstract class AudiovisualesDataBase: RoomDatabase() {
    abstract fun docentesDao():DocentesDAO
    abstract fun implementosDao():ImplementosDAO
    abstract fun encargadosDao(): EncargadosDAO
    abstract fun prestamosDao(): PrestamosDAO

    companion object{
        @Volatile
        private var INSTANCE : AudiovisualesDataBase?= null

        fun getDatabase(context: Context): AudiovisualesDataBase{
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    AudiovisualesDataBase::class.java,
                    "DB_AUDIOVISUALES"
                ).build()
                INSTANCE= instance
                instance
            }
        }
    }
}