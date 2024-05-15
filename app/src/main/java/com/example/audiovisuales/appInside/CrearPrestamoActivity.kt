package com.example.audiovisuales.appInside

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Prestamos
import com.example.audiovisuales.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CrearPrestamoActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_prestamo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db= AudiovisualesDataBase.getDatabase(this)

        val spinnerDocentes= findViewById<Spinner>(R.id.spinnerDocentes);
        val spinnerImplementos= findViewById<Spinner>(R.id.spinnerImplementos);
        val btnRegistrarPrestamo= findViewById<Button>(R.id.btnRegistrarPrestamo);
        val txtObservacionPrestamo= findViewById<EditText>(R.id.txtObservacionPrestamo)

        val btnBackToList= findViewById<ImageButton>(R.id.btnBackToList)

        //traer nombres de implementos y docentes para adaptar a spinners

        GlobalScope.launch(Dispatchers.IO) {
            // Accede a la base de datos aquí
            val listaDocentes = db.docentesDao().seleccionarTodoDocentes().map { it.nombres }
            val listaImplementos = db.implementosDao().seleccionarTodoImplementos().map { it.nombre }

            // Actualiza la interfaz de usuario en el hilo principal después de obtener los datos
            runOnUiThread {
                // Configura los adaptadores de los spinners aquí
                val adaptadorDocentes = createCustomSpinnerAdapter(this@CrearPrestamoActivity, listaDocentes)
                spinnerDocentes.adapter = adaptadorDocentes

                val adaptadorImplementos = createCustomSpinnerAdapter(this@CrearPrestamoActivity, listaImplementos)
                spinnerImplementos.adapter = adaptadorImplementos
            }
        }




        //datos del usuario en sesion
        val (nombre, cedula)= obtenerDatosUsuario(this)
        val cedulaEncargado= cedula
        val nombreEncargado= nombre

        var codigoImplemento: Long?= null
        var cedulaDocente: String?= null
        var nombreDocente: String?= null
        var fecha: String?=null
        var estado: Int=0;
        var observacion: String?= null


//cada que haya cambios en el spiner se asignaran automaticamente los datos de la seleccion a las variables
        obtenerSeleccion(spinnerDocentes) { seleccion, posicion ->
            GlobalScope.launch(Dispatchers.IO) {
                val docenteSeleccionado = db.docentesDao().seleccionarTodoDocentes()[posicion]
                runOnUiThread {
                    cedulaDocente = docenteSeleccionado.cedulaDocente
                    nombreDocente = docenteSeleccionado.nombres
                }
            }
        }

        obtenerSeleccion(spinnerImplementos) { seleccion, posicion ->
            GlobalScope.launch(Dispatchers.IO) {
                val itemSeleccionado = db.implementosDao().seleccionarTodoImplementos()[posicion]
                runOnUiThread {
                    codigoImplemento = itemSeleccionado.codigo
                }
            }
        }




        btnRegistrarPrestamo.setOnClickListener {
            fecha=obtenerFechaHoraActual();
            if(!txtObservacionPrestamo.text.isEmpty()){
                observacion= txtObservacionPrestamo.text.toString()
            }else{
                observacion= "Ninguna..."
            }
            var PRESTAMO= Prestamos(cedulaEncargado = cedulaEncargado!!, codigoImplemento = codigoImplemento.toString(), cedulaDocente = cedulaDocente.toString(),
            fechaHora = fecha!!, estado = estado, observaciones = observacion!!)
            GlobalScope.launch(Dispatchers.IO){
                try {
                    db.prestamosDao().insertarPrestamo(PRESTAMO)
                    runOnUiThread{
                        Toast.makeText(applicationContext, "Prestamo Registrado", Toast.LENGTH_SHORT).show()
                    }
                }catch (ex:Exception){
                    runOnUiThread{
                        Toast.makeText(applicationContext, "error de registro: $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            backToList()
        }

        btnBackToList.setOnClickListener {
            backToList()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaHoraActual(): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        return fechaHoraActual.format(formato)
    }

    fun obtenerSeleccion(spinner: Spinner, callback: (String, Int) -> Unit) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val seleccion = parent?.getItemAtPosition(position).toString()
                callback(seleccion, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }




    fun createCustomSpinnerAdapter(context: Context, options: List<String>): ArrayAdapter<String> {
        return object : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createItemView(position, convertView, parent)
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createItemView(position, convertView, parent)
            }

            private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
                val textView = super.getView(position, convertView, parent) as TextView
                textView.text = options[position]
                return textView
            }
        }
    }

    fun backToList(){
        var intent= Intent(this, PrestamosMainActivity::class.java)
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