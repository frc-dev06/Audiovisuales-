package com.example.audiovisuales.appInside

import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Docentes
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.docentes.DocentesMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrarDocenteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_docente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db= AudiovisualesDataBase.getDatabase(this)
        val btnBack= findViewById<ImageButton>(R.id.btnBackToPrimaryActivity);
        val btnRegistrar= findViewById<Button>(R.id.btnRegistrarDocente);
        val btnVerDocentes= findViewById<Button>(R.id.btnBuscarDocentes);

        val txtCedulaDocente= findViewById<EditText>(R.id.txtCedulaDocente);
        val txtNombreDocente= findViewById<EditText>(R.id.txtNombreDocente);
        val spinnerFacultades= findViewById<Spinner>(R.id.LsFacultades);

        val facultades= listOf("ISUM", "COP", "INGR", "INDUS", "ADMI", "CED")

        val adaptadorFacultades= createCustomSpinnerAdapter(this, facultades)
        spinnerFacultades.adapter= adaptadorFacultades

        var facultadSelect: String?= null


        obtenerSeleccion(spinnerFacultades){ seleccion, posicion ->
            facultadSelect=facultades[posicion]
           // Toast.makeText(this, "$facultadSelect", Toast.LENGTH_SHORT).show()
        }


        btnBack.setOnClickListener {
            backToPrimaryActivity()
        }

        fun registrarDocente() {
            if (!txtCedulaDocente.text.isEmpty() && !txtNombreDocente.text.isEmpty()) {
                val cedula = txtCedulaDocente.text.toString()
                val nombre = txtNombreDocente.text.toString()
                val docenteNuevo = Docentes(cedula, nombre, facultadSelect!!)

                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        db.docentesDao().insertarDocente(docenteNuevo)
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Se registró el profesor $nombre", Toast.LENGTH_SHORT).show()
                            limpiarTextos(txtNombreDocente, txtCedulaDocente)
                        }
                    } catch (ex: Exception) {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Error al registrar: $ex", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


        btnRegistrar.setOnClickListener {
            registrarDocente()
        }

        btnVerDocentes.setOnClickListener {
            goToDocentes()
        }
    }

    fun limpiarTextos(txtCedula: TextView, txtNombre: TextView){
        txtCedula.text=""
        txtNombre.text=""
    }

    fun backToPrimaryActivity(){
        var intent= Intent(this, primaryActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToDocentes(){
        var intent= Intent(this, DocentesMainActivity::class.java)
        startActivity(intent)
        this.finish()
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
}