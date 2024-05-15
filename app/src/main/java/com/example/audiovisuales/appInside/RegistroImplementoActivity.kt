package com.example.audiovisuales.appInside

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Implementos
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.implementos.implementosMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistroImplementoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_implemento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val db= AudiovisualesDataBase.getDatabase(this)
        val txtNombreImplemento= findViewById<EditText>(R.id.txtNombreImplemento);
        val txtDescripcion= findViewById<EditText>(R.id.txtDescripcionImplemento);

        val btnRegistrar= findViewById<Button>(R.id.btnRegistrarImplemento);
        val btnVerImplementos= findViewById<Button>(R.id.btnVerImplementos);

        val btnBack= findViewById<ImageButton>(R.id.btnBackToPrymari);

        btnBack.setOnClickListener {
            backToPrimaryActivity()
        }

        fun registrarImplemento(){
            if (!txtNombreImplemento.text.isEmpty() && !txtDescripcion.text.isEmpty()){
                val implemento= txtNombreImplemento.text.toString();
                val descripcion= txtDescripcion.text.toString();
                val implementoNuevo= Implementos(nombre = implemento, caracteristicas = descripcion)

                GlobalScope.launch(Dispatchers.IO){
                    try {
                        db.implementosDao().insertarImplemento(implementoNuevo)
                        runOnUiThread{
                            Toast.makeText(applicationContext, "$implemento registrado", Toast.LENGTH_SHORT).show()
                            limpiarTextos(txtNombreImplemento, txtDescripcion)
                        }
                    }catch (ex: Exception){
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Algo salio mal: $ex", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegistrar.setOnClickListener {
            registrarImplemento()
        }
        btnVerImplementos.setOnClickListener {
            goToVerImplementos()
        }

    }

    fun limpiarTextos(txtImplemento: TextView, txtDescripcion: TextView){
        txtImplemento.text=""
        txtDescripcion.text=""
    }

    fun backToPrimaryActivity(){
        var intent= Intent(this, primaryActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToVerImplementos(){
        var intent= Intent(this, implementosMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}