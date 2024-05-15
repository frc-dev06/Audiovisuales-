package com.example.audiovisuales.appInside

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.MainActivity
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.reportes.ReportesMainActivity

class primaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_primary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegistrarDocente= findViewById<Button>(R.id.btnRegistroDocente);
        val btnRegistrarImplemento= findViewById<Button>(R.id.btnRegistroImplemento);
        val btnVerPrestamos= findViewById<Button>(R.id.btnPrestamos);
        val btnReportes= findViewById<Button>(R.id.btnReportes);


        //traer nombre de usuario del activity anterior
        val txtHola= findViewById<TextView>(R.id.txtHola);
        val (nombre, cedula)= obtenerDatosUsuario(this)
        txtHola.text= "Hola $nombre"

        btnRegistrarDocente.setOnClickListener {
            openRegistroDocente()
        }
        btnRegistrarImplemento.setOnClickListener {
            openRegistroImplementos()
        }
        btnVerPrestamos.setOnClickListener {
            openPrestamos()
        }
        btnReportes.setOnClickListener {
            openReportes()
        }
    }

    fun openRegistroDocente(){
        var intent= Intent(this, RegistrarDocenteActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun openRegistroImplementos(){
        var intent= Intent(this, RegistroImplementoActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun openPrestamos(){
        var intent= Intent(this, PrestamosMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun openReportes(){
        var intent= Intent(this, ReportesMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun obtenerDatosUsuario(context: Context): Pair<String?, String?> {
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val nombre = sharedPreferences.getString("nombre", null)
        val cedula = sharedPreferences.getString("cedulaEncargado", null)
        return Pair(nombre, cedula)
    }
}