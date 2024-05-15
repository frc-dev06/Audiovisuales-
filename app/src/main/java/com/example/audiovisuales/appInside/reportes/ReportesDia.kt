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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ReportesDia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportes_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val puntos= ArrayList<Bar>()


        val btnBack= findViewById<ImageButton>(R.id.btnBackToReports)

        val db= AudiovisualesDataBase.getDatabase(this)

        btnBack.setOnClickListener {
            back()
        }

        var prestamoPorDias:List<ReportModels.PrestamoPorDia> = emptyList()


            GlobalScope.launch(Dispatchers.IO){
                try {
                    val prestamoPorDias = db.prestamosDao().obtenerCantidadPrestamosPorDiasSemana()
                    if (prestamoPorDias.isEmpty()) {
                        println("No se encontraron préstamos.")
                    } else {
                        prestamoPorDias.forEach { println("${it.diaSemana} --- ${it.cantidad}") }
                        graficar(prestamoPorDias, puntos)
                    }
                } catch (e: Exception) {
                    println("Error al obtener préstamos: ${e.message}")
                    e.printStackTrace()
                }

            }




    }

    fun graficar(lista: List<ReportModels.PrestamoPorDia>, puntos:ArrayList<Bar>){

        for (i in lista){
            val barra= Bar()
            var color= generarColorAleatorio()
            barra.color= Color.parseColor(color)
            barra.name= i.diaSemana
            barra.value= i.cantidad.toFloat()

            puntos.add(barra)
        }
        val grafica= findViewById<View>(R.id.graphBarDias) as BarGraph
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
    fun back(){
        var intent= Intent(this, ReportesMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}