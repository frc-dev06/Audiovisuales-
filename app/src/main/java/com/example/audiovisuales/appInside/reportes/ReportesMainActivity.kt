package com.example.audiovisuales.appInside.reportes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.primaryActivity

class ReportesMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportes_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRepoDia= findViewById<Button>(R.id.btnRepoDia)
        val btnRepoDoc= findViewById<Button>(R.id.btnRepoDocentes)
        val btnRepoImp=findViewById<Button>(R.id.btnRepoImplementos)
        val btnback= findViewById<ImageButton>(R.id.btnBackToMenuRepor)


        btnRepoDia.setOnClickListener {
            goToRepoDia()
        }
        btnRepoDoc.setOnClickListener {
            goToRepoDocente()
        }
        btnRepoImp.setOnClickListener {
            goToRepoImplemento()
        }
        btnback.setOnClickListener {
            back()
        }

    }

    fun back(){
        var intent= Intent(this, primaryActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToRepoDia(){
        var intent= Intent(this, ReportesDia::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToRepoDocente(){
        var intent= Intent(this, ReportesDocente::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToRepoImplemento(){
        var intent= Intent(this, ReportesImplemento::class.java)
        startActivity(intent)
        this.finish()
    }

}