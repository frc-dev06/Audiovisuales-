package com.example.audiovisuales.appInside

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.PrestamosWithDetails
import com.example.audiovisuales.R
import com.example.audiovisuales.adapters.prestamoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PrestamosMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prestamos_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db= AudiovisualesDataBase.getDatabase(this)



        val lista = findViewById<ListView>(R.id.listViewPrestamos)
        val btnBack = findViewById<ImageButton>(R.id.btnBackTO)
        val btnAdd = findViewById<ImageButton>(R.id.btnAddPrestamo)

        var listaPrestamos: List<PrestamosWithDetails> = emptyList()

        GlobalScope.launch(Dispatchers.IO) {

            listaPrestamos = db.prestamosDao().seleccionarDetallesPrestamo()

            runOnUiThread {
                val adapter = prestamoAdapter(this@PrestamosMainActivity, listaPrestamos)
                lista.adapter = adapter
            }
        }



        lista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@PrestamosMainActivity, PrestamoActivity::class.java)

            if (position < listaPrestamos.size) {
                intent.putExtra("prestamo", listaPrestamos[position])
                startActivity(intent)
            } else {

            }
        }

        btnBack.setOnClickListener {
            backToPrimaryActivity()
        }

        btnAdd.setOnClickListener {
            goAddPrestamo()
        }
    }


    fun backToPrimaryActivity(){
        var intent= Intent(this, primaryActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goAddPrestamo(){
        var intent= Intent(this, CrearPrestamoActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}