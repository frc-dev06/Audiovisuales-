package com.example.audiovisuales.appInside.implementos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Implementos
import com.example.audiovisuales.R
import com.example.audiovisuales.adapters.implementosAdapter
import com.example.audiovisuales.appInside.RegistroImplementoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class implementosMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_implementos_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db= AudiovisualesDataBase.getDatabase(this)
        val lista= findViewById<ListView>(R.id.listViewImplements)
        val btnBack= findViewById<ImageButton>(R.id.btnBackToImplements)

        var listaImplementos: List<Implementos> = emptyList()

        GlobalScope.launch(Dispatchers.IO){
            listaImplementos= db.implementosDao().seleccionarTodoImplementos()

            runOnUiThread {
                val adapter= implementosAdapter(this@implementosMainActivity, listaImplementos)
                lista.adapter= adapter
            }
        }

        btnBack.setOnClickListener {
            backToRegistrarImplement()
        }
    }

    fun backToRegistrarImplement(){
        var intent= Intent(this, RegistroImplementoActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}