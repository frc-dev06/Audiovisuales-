package com.example.audiovisuales.appInside.reportes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.primaryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ReportesDocente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportes_docente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack= findViewById<ImageButton>(R.id.btnBackToFrecDoc)
        val db= AudiovisualesDataBase.getDatabase(this)
        var listaPrestamosDocente: List<ReportModels.ReporteDocentePrestamos> = emptyList()
        val puntos= ArrayList<Bar>()

        GlobalScope.launch(Dispatchers.IO){
            try {
                listaPrestamosDocente = db.prestamosDao().obtenerCantidadPrestamosPorDocente()
                listaPrestamosDocente.map { println("nombre: ${it.nombreDocente} cantidad: ${it.cantidadPrestamos}") }
                graficar(listaPrestamosDocente, puntos)
            }catch (e: Exception){
                print("Error en la consulta de docentes: $e")
            }
        }

        btnBack.setOnClickListener {
            goToBack()
        }
    }

    fun graficar(lista: List<ReportModels.ReporteDocentePrestamos>, puntos:ArrayList<Bar>){

        for (i in lista){
            val barra= Bar()
            var color= generarColorAleatorio()
            barra.color= Color.parseColor(color)
            barra.name= i.nombreDocente
            barra.value= i.cantidadPrestamos.toFloat()

            puntos.add(barra)
        }
        val grafica= findViewById<View>(R.id.chartDocentes) as BarGraph
        grafica.bars=puntos
    }

    fun generarColorAleatorio():String{
        val letras= arrayOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
        var color="#"
        for (i in 0..5){
            color+= letras[(Math.random()*15).roundToInt()]
        }
        return color
    }

    fun goToBack(){
        var intent= Intent(this, ReportesMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}