package com.example.audiovisuales.appInside.docentes

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Docentes
import com.example.audiovisuales.Entities.PrestamosWithDetails
import com.example.audiovisuales.R
import com.example.audiovisuales.adapters.docenteAdapter
import com.example.audiovisuales.adapters.prestamoAdapter
import com.example.audiovisuales.appInside.RegistrarDocenteActivity
import com.example.audiovisuales.appInside.primaryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DocentesMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_docentes_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var db= AudiovisualesDataBase.getDatabase(this)
        val btnBack= findViewById<ImageButton>(R.id.btnBackToDocentes)

        var lista= findViewById<ListView>(R.id.listViewDocentes);

        var listaDocentes: List<Docentes> = emptyList()

        GlobalScope.launch(Dispatchers.IO) {

            listaDocentes = db.docentesDao().seleccionarTodoDocentes()
            runOnUiThread {
                val adapter = docenteAdapter(this@DocentesMainActivity, listaDocentes)
                lista.adapter = adapter
            }
        }

        btnBack.setOnClickListener {
            backToResgistrarDocentes()
        }

    }

    fun backToResgistrarDocentes(){
        var intent= Intent(this, RegistrarDocenteActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}